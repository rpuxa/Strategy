package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Fallible

interface Unit : FieldObject, Buildable {
    var health: Int
    var movePoints: Int

    val maxMovePoints: Int

    companion object : Fallible {

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