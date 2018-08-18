package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.NO_PLAYER_COLOR_CELL
import ru.rpuxa.strategy.NULL_COLOR_CELL
import ru.rpuxa.strategy.field.interfaces.StaticObject
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.players.Player

/**
 * Клетка поля игры [Field]
 */
class Cell(

        /**
         *  Статический объект, которых находится на данной клетке
         *
         *  [StaticObject.EMPTY] - для обозначения отсутствия
         */
        var obj: StaticObject = StaticObject.EMPTY,

        /**
         *  Юнит, который находится на этой клетке
         *
         *  [Unit.NONE] - для обозначения отсутствия
         */
        var unit: Unit = Unit.NONE,

        /**
         *  Игрок, владеющий клеткой
         *
         *  [Player.NONE] - для обозначения отсутствия
         */
        var owner: Player = Player.NONE,
        override var x: Int,
        override var y: Int
) : Location {

    /**
     * Цвет клетки
     */
    val color: Int
        get() = when {
            this === NONE -> NULL_COLOR_CELL
            owner === Player.NONE -> NO_PLAYER_COLOR_CELL
            else -> owner.color
        }
    /**
     * Может ли юнит пройти мимо данной клетки (не останавливаясь)
     */
    val canPass: Boolean
        get() = obj.passable && this != NONE

    /**
     * Может ли юнит остановится в данной клетке
     */
    val canStop: Boolean
        get() = unit == Unit.NONE && canPass


    companion object {

        /**
         * Константа, обозначающая отсутствие клетки
         *
         * Применяется, за границами поля [Field]
         */
        val NONE = Cell(StaticObject.EMPTY, x = -10, y = -10)
    }
}