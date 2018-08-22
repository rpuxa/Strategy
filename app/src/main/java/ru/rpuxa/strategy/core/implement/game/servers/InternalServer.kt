package ru.rpuxa.strategy.core.implement.game.servers

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.field.units.Colonist
import ru.rpuxa.strategy.core.implement.field.units.Swordsman
import ru.rpuxa.strategy.core.implement.game.players.Human
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.MutableField
import ru.rpuxa.strategy.core.interfaces.field.info.BuildableInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.interfaces.game.Server
import ru.rpuxa.strategy.core.others.*
import kotlin.reflect.KClass

/**
 * Реализация Server для оффлайн игр
 */
class InternalServer(val field: MutableField) : Server {
    override lateinit var players: Array<Player>


    override var turn = 0

    override var started = false

    override fun start() {
        if (started)
            throw IllegalStateException("InternalServer already started!")
        started = true

        val humans = players.filter { it is Human }
        if (humans.size > 1) {
            throw IllegalStateException("No more 1 human in game!")
        }

        field[4, 0].staticObject = Town(4 loc 0, PLAYER_RED)
        field.setUnit(Swordsman(0 loc 0, players[0]))
        val unit = Swordsman(4 loc 0, PLAYER_RED)
        unit.health = 40
        field.setUnit(unit)

        sendAll { it.onStart() }
        players[turn].onMoveStart()
    }

    override fun moveUnit(unit: Unit, toLocation: Location, sender: Player) {
        if (sender.checkTurn())
            return

        val move = field.getUnitMoves(unit).find { it.cell.x == toLocation.x && it.cell.y == toLocation.y }

        when {
            move == null -> sender.onRuleViolate(Server.Rules.invalidMove)
            move.steps > unit.movePoints -> sender.onRuleViolate(Server.Rules.enoughMovePoints)
            else -> {
                unit.movePoints -= move.steps
                val from = unit.copyLocation()
                field.changeLocationUnit(unit, toLocation)
                val fieldAfterMove = field.copy()
                sendAll { it.onMoveUnit(from, toLocation, sender, fieldAfterMove) }
                unitEnterTown(unit)
            }
        }
    }

    private fun unitEnterTown(unit: Unit) {
        val town = field[unit].staticObject as? Town ?: return
        if (town.owner != town.owner) {
            town.movesToDestroy = 2
            val fieldAfterSeize = field.copy()
            sendAll { it.onSeizeTown(town, fieldAfterSeize) }
        } else {
            town.movesToDestroy = -1
            val fieldAfterSeize = field.copy()
            sendAll { it.onStifleRebellion(town, fieldAfterSeize) }
        }
    }

    override fun build(buildableInfo: BuildableInfo, clazz: KClass<out BuildableObject>, location: Location, town: Town, sender: Player) {
        if (sender.checkTurn())
            return
        when {
            town.workPoints < buildableInfo.cost -> sender.onRuleViolate(Server.Rules.enoughMoney(buildableInfo, town))
            town.bought -> sender.onRuleViolate(Server.Rules.secondTimeBuilding)
            else -> {
                town.workPoints -= buildableInfo.cost
                town.bought = true

                val constructor = clazz.java.constructors[0]

                val buildable = constructor.newInstance(location, sender) as BuildableObject

                when (buildable) {
                    is Unit -> field[buildable].unit = buildable
                    is StaticObject -> field[buildable].staticObject = buildable
                    else -> throw IllegalStateException()
                }

                sendAll { it.onBuild(buildable) }
            }
        }
    }

    override fun layTown(location: Location, sender: Player) {
        if (sender.checkTurn())
            return

        val cell = field[location]
        when {
            cell.unit !is Colonist -> sender.onRuleViolate(Server.Rules.noColonistLayTown)
            cell.staticObject != STATIC_OBJECT_NONE -> sender.onRuleViolate(Server.Rules.layTownNoEmptyCell)
            cell.unit.movePoints == 0 -> sender.onRuleViolate(Server.Rules.enoughMovePoints)
            else -> {
                cell.unit = UNIT_NONE
                cell.staticObject = Town(location, sender)
                field.getTownTerritory(cell.staticObject as Town).forEach { it.owner = sender }

                sendAll { it.onTownLaid(location) }
            }
        }
    }

    override fun attack(defender: Unit, attacker: Unit, sender: Player) {
        if (sender.checkTurn())
            return

        //TODO провека на атаку
        val from = attacker.copyLocation()
        val moves = field.getUnitMoves(attacker)
        val attackFrom = moves.find { it.cell equals defender }?.lastCell!!
        field.changeLocationUnit(attacker, attackFrom)
        val defenderHealth = defender.fight(attacker)
        val attackerHealth = attacker.fight(defender)
        val defenderHit = defender.health - defenderHealth
        val attackerHit = attacker.health - attackerHealth
        if (defenderHealth > 0)
            field[defender].unit.health = defenderHealth
        else
            field[defender].unit = UNIT_NONE

        var killed = false
        if (attackerHealth > 0) {
            field[attacker].unit.health = attackerHealth
            if (defenderHealth <= 0){
                field.changeLocationUnit(attacker, defender)
                killed = true
            }
        }
        else
            field[attacker].unit = UNIT_NONE

        attacker.movePoints = 0
        val fieldAfterAttack = field.copy()


        sendAll { it.onAttack(from, attackFrom, attacker, defender, defenderHit, attackerHit, killed, fieldAfterAttack) }
        unitEnterTown(attacker)
    }

    override fun endMove(sender: Player) {
        if (sender.checkTurn())
            return
        turn++
        if (turn == players.size)
            turn = 0
        field.forEach {
            if (it.unit != UNIT_NONE)
                it.unit.movePoints = it.unit.baseMovePoints
            val obj = it.staticObject
            if (obj is Town) {
                obj.workPoints += obj.performance
                obj.addDay()
                obj.bought = false
            }
        }
        players[turn].onMoveStart()
    }

    private fun Player.checkTurn(): Boolean {
        if (!isHisTurn) {
            onRuleViolate(Server.Rules.moveBeforeYouTurn)
            return true
        }
        return false
    }

    private val Player.isHisTurn: Boolean
        get() = players[0] == this

    private inline fun sendAll(block: (Player) -> kotlin.Unit) = players.forEach(block)
}
