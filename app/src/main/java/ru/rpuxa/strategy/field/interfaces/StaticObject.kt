package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Fallible

/**
 * Статический объект на поле, которые
 * не может изменять своего расположения
 */
interface StaticObject : FieldObject {
    /**
     * Может ли юнит [Unit] пройти через этот объект
     *
     * Например горы не являются проходимым объектом
     */
    val passable: Boolean

    companion object : Fallible {
        /**
         * Константа которая показывает отсутствие статического объекта на клетке
         *
         * Все методы не реализованы, кроме [passable], т.к через
         * пустую клетку можно пройти юнитом
         */
        val EMPTY = object : NaturalStructures {
            override val icon: Int
                get() = fail()
            override val passable = true
            override var x: Int
                set(_) = fail()
                get() = fail()
            override var y: Int
                set(_) = fail()
                get() = fail()
            override val description: String
                get() = fail()
            override val name: String
                get() = fail()

        }
    }
}