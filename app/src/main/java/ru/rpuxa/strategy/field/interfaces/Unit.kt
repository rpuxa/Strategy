package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Fallible

/**
 * Юнит. Может перемещаться по полю
 */
interface Unit : FieldObject, Buildable {

    /**
     * Текущее здоровье юнита
     * При достижении 0 юнит умирает
     */
    var health: Int

    /**
     *  Сколько клеток может пройти юнит.
     *  Восстанавливаются при начале нового хода
     */
    var movePoints: Int

    /**
     * Начальное количество [movePoints]
     */
    val maxMovePoints: Int

    companion object : Fallible {

        /**
         * Константа которая показывает отсутствие юнита на клетки
         *
         * Все методы не реализованы
         */
        val NONE = object : Unit {
            override val cost: Int
                get() = fail()
            override val description: String
                get() = fail()
            override val name: String
                get() = fail()
            override var movePoints: Int
                get() = fail()
                set(_) = fail()
            override val maxMovePoints: Int
                get() = fail()
            override var x: Int
                get() = fail()
                set(_) = fail()
            override var y: Int
                get() = fail()
                set(_) = fail()
            override var health: Int
                get() = fail()
                set(_) = fail()
            override val icon: Int
                get() = fail()
        }
    }
}