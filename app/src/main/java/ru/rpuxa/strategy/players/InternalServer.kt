package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.copyLocation
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.MutableField
import ru.rpuxa.strategy.field.interfaces.StaticObject
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.field.units.Colonist

/**
 * Реализация Server для оффлайн игр
 */
class InternalServer(val field: MutableField) : Server {
    override lateinit var players: Array<Player>
    override val controllingHuman: Human?
        get() = players.find { it is Human } as Human?

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

        field[2, 2].unit = Colonist(2, 2)

        sendAll { it.onStart() }
        players[turn].onMoveStart()
    }

    override fun moveUnit(unit: Unit, toLocation: Location, sender: Player) {
        if (sender.checkTurn())
            return

        val move = field.getUnitMoves(unit).find { it.cell.x == toLocation.x && it.cell.y == toLocation.y }

        when {
            move == null -> sender.onRuleViolate(Rules.invalidMove)
            move.steps > unit.movePoints -> sender.onRuleViolate(Rules.enoughMovePoints)
            else -> {
                unit.movePoints -= move.steps
                val from = unit.copyLocation()
                field.changeLocationUnit(unit, toLocation)
                sendAll { it.onMoveUnit(from, toLocation, sender) }
            }
        }
    }

    override fun build(buildable: Buildable, town: Town, sender: Player) {
        if (sender.checkTurn())
            return
        when {
            town.workPoints < buildable.cost -> sender.onRuleViolate(Rules.enoughMoney(buildable, town))
            town.bought -> sender.onRuleViolate(Rules.secondTimeBuilding)
            else -> {
                town.workPoints -= buildable.cost
                town.bought = true

                when (buildable) {
                    is Unit -> field[buildable].unit = buildable
                    is StaticObject -> field[buildable].obj = buildable
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
            cell.unit !is Colonist -> sender.onRuleViolate(Rules.noColonistLayTown)
            cell.obj != StaticObject.EMPTY -> sender.onRuleViolate(Rules.layTownNoEmptyCell)
            cell.unit.movePoints == 0 -> sender.onRuleViolate(Rules.enoughMovePoints)
            else -> {
                cell.unit = Unit.NONE
                cell.obj = Town(location.x, location.y, sender)
                field.getTownTerritory(cell.obj as Town).forEach { it.owner = sender }

                sendAll { it.onTownLaid(location) }
            }
        }
    }

    override fun endMove(sender: Player) {
        if (sender.checkTurn())
            return
        turn++
        if (turn == players.size)
            turn = 0
        field.forEach {
            if (it.unit != Unit.NONE)
                it.unit.movePoints = it.unit.maxMovePoints
            val obj = it.obj
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
            onRuleViolate(Rules.moveBeforeYouTurn)
            return true
        }
        return false
    }

    private val Player.isHisTurn: Boolean
        get() = players[0] == this

    private inline fun sendAll(block: (Player) -> kotlin.Unit) = players.forEach(block)
}
