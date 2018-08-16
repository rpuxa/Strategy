package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.*
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.visual.FieldVisualizer

class InternalServer(val field: MutableField) : CommandExecutor {
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

        sendAll { it.onStart() }
    }

    override fun moveUnit(unit: Unit, location: Location, sender: Player) {
        if (sender.checkTurn())
            return
        //TODO проверку на легальность хода

        val from = unit.copyLocation()
        field.changeLocationUnit(unit, location)
        sendAll { it.onMoveUnit(from, location, sender) }
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


    val alone: InternalServer
        get() {
            val game = InternalServer(board!!)
            val human = Human(game, game.field, Color.YELLOW, visual)
            game.players = arrayOf(human)
            visual.setControllingHuman(human)
            return game
        }
}