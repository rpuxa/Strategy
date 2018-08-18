package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.Fallible
import ru.rpuxa.strategy.field.interfaces.Field
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.objects.player.Town

interface Player {
    val executor: CommandExecutor
    val field: Field
    val color: Int
    val townsCount: Int
        get() = this@Player.field.count { it.obj is Town && it.owner === this }


    fun onRuleViolate(rule: RuleException)

    fun onMoveUnit(from: Location, to: Location, sender: Player)

    fun onStart()

    fun onMoveStart()

    fun onBuild(buildable: Buildable)

    fun onTownLaid(location: Location)


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

            override fun onStart() = fail()

            override fun onMoveStart() = fail()

            override fun onBuild(buildable: Buildable) = fail()

            override fun onTownLaid(location: Location) = fail()
        }

        val RED: Player = object : Player by NONE {
            override val color = Color.RED
        }
    }
}

