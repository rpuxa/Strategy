package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.Fallible
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Location

interface Player {
    val executor: CommandExecutor
    val field: Field
    val color: Int

    fun onRuleViolate(rule: RuleException)

    fun onMoveUnit(from: Location, to: Location, sender: Player)

    fun onStart()

    fun onMoveStart()


    companion object : Fallible {

        val NONE = object : Player {

            override val executor: CommandExecutor
                get() = fail()
            override val field: Field
                get() = fail()
            override val color: Int
                get() = fail()

            override fun onRuleViolate(rule: RuleException) = fail()

            override fun onMoveUnit(from: Location, to: Location, sender: Player) = fail()

            override fun onStart() {
                fail()
            }

            override fun onMoveStart() {
                fail()
            }
        }

        val RED: Player = object : Player by NONE {
            override val color = Color.RED
        }
    }
}

