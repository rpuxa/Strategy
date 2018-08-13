package ru.rpuxa.strategy

import android.graphics.Color
import ru.rpuxa.strategy.field.GameBoardCreator
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.players.CommandExecutor
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.players.Player
import ru.rpuxa.strategy.views.FieldView

class Game(val visual: FieldView, val board: HexagonField) : CommandExecutor {

    lateinit var players: Array<Player>

    init {
        val humans = players.filter { it is Human }
        if (humans.size > 1) {
            throw IllegalStateException("No more 1 human in game!")
        }
        if (humans.isNotEmpty()) {
            val human = humans[0] as Human
            visual.setControlHuman(human)
        }
    }


}


class GameBuilder(var visual: FieldView, var board: HexagonField?) {

    init {
        if (board == null) {
            board = GameBoardCreator.square(10, 5)
        }
    }


    fun alone(): Game {
        val game = Game(visual, board!!)
        game.players = arrayOf(Human(game, Color.YELLOW))
        return game
    }
}