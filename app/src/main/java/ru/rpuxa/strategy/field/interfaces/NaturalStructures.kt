package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Fallible

interface NaturalStructures : FieldObject {

    companion object : Fallible {
        val EMPTY: FieldObject = object : NaturalStructures {
            override val icon: Int
                get() = fail()
            override val passable = true
            override val x: Int
                get() = fail()
            override val y: Int
                get() = fail()

        }
    }
}