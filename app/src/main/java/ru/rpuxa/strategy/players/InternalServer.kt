package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.*
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.MutableField
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.visual.FieldVisualizer

class InternalServer(val field: MutableField) : CommandExecutor {
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

        sendAll { it.onStart() }
    }

    override fun moveUnit(unit: Unit, location: Location, sender: Player) {
        if (sender.checkTurn())
            return

        val move = field.getUnitMoves(unit).find { it.cell.x == location.x && it.cell.y == location.y }

        if (move == null || move.steps > unit.movePoints) {
            sender.onRuleViolate(Rules.invalidMove())
            return
        }

        unit.movePoints -= move.steps

        val from = unit.copyLocation()
        field.changeLocationUnit(unit, location)
        sendAll { it.onMoveUnit(from, location, sender) }
    }

    override fun build(buildable: Buildable, town: Town, sender: Player) {
        if (sender.checkTurn())
            return
        if (town.workPoints < buildable.cost) {
            sender.onRuleViolate(Rules.enoughMoney(buildable, town))
            return
        }
        if (town.bought) {
            sender.onRuleViolate(Rules.secondTimeBuilding())
            return
        }
        town.workPoints -= buildable.cost
        town.bought = true

        sendAll { it.onBuild(buildable) }
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
        }
        players[turn].onMoveStart()
    }

    private fun Player.checkTurn(): Boolean {
        if (!isHisTurn) {
            onRuleViolate(Rules.moveBeforeYouTurn())
            return true
        }
        return false
    }

    private val Player.isHisTurn: Boolean
        get() = players[0] == this

    private inline fun sendAll(block: (Player) -> kotlin.Unit) = players.forEach(block)
}


class GameBuilder(private var visual: FieldVisualizer, private var board: HexagonField? = null) {

    init {
        if (board == null) {
            board = GameBoardCreator.square(10, 5)
        }
    }


    fun alone(): InternalServer {
        val game = InternalServer(board!!)
        val human = Human(game, game.field, Color.YELLOW, visual)
        game.players = arrayOf(human)
        visual.setControllingHuman(human)
        return game
    }
}