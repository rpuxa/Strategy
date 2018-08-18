package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.GameFieldCreator
import ru.rpuxa.strategy.field.interfaces.MutableField
import ru.rpuxa.strategy.visual.FieldVisualizer

/**
 *  Строитель игр. Создаёт сервер по определённому шаблону
 */
class GameBuilder(private var visual: FieldVisualizer, private var board: MutableField? = null) {

    init {
        if (board == null)
            board = GameFieldCreator.rectangle(10, 5)
    }

    /**
     * Шаблон для дебага. Никого нет кроме 1 человека
     */
    fun alone(): InternalServer {
        val game = InternalServer(board!!)
        val human = Human(game, game.field, Color.RED, visual)
        game.players = arrayOf(human)
        visual.setControllingHuman(human)
        return game
    }
}
