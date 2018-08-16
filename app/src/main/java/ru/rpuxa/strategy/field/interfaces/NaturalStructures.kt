package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Fallible

interface NaturalStructures : FieldObject {

    companion object : Fallible {
        val EMPTY: FieldObject = object : NaturalStructures {
            override val icon: Int
                get() = fail()
            override val passable = true
            override var x: Int
                set(value) = fail()
                get() = fail()
            override var y: Int
                set(value) = fail()
                get() = fail()

        }
    }
}