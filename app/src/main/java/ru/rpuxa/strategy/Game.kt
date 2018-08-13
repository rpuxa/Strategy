package ru.rpuxa.strategy

import android.graphics.Color
import ru.rpuxa.strategy.field.GameBoardCreator
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.players.CommandExecutor
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.players.Player
import ru.rpuxa.strategy.visual.FieldView

class Game(val visual: FieldView, val board: HexagonField) : CommandExecutor {

    lateinit var players: Array<Player>
    var started = false

    override fun start() {
        if (started)
            throw IllegalStateException("Game already started!")
        started = true

        val humans = players.filter { it is Human }
        if (humans.size > 1) {
            throw IllegalStateException("No more 1 human in game!")
        }
        if (humans.isNotEmpty()) {
            val human = humans[0] as Human
            visual.setControlHuman(human)
        }

        visual.draw(board)
    }
}


class GameBuilder(var visual: FieldView, var board: HexagonField? = null) {

    init {
        if (board == null) {
            board = GameBoardCreator.square(10, 5)
        }
    }


    val alone: Game
        get() {
            val game = Game(visual, board!!)
            game.players = arrayOf(Human(game, game.board, Color.YELLOW))
            return game
        }
}