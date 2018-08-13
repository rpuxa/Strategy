package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.Fallible
import ru.rpuxa.strategy.field.Field

interface Player {
    val executor: CommandExecutor
    val field: Field
    val color: Int




    companion object : Fallible {

        val NONE = object : Player {
            override val executor: CommandExecutor
                get() = fail()
            override val field: Field
                get() = fail()
            override val color: Int
                get() = fail()
        }
    }
}

