package ru.rpuxa.strategy.field.objects.player

import ru.rpuxa.strategy.INITIAL_PERFORMANCE
import ru.rpuxa.strategy.TOWN_COUNT_PERFORMANCE_FACTOR
import ru.rpuxa.strategy.TOWN_LEVEL_PERFORMANCE_FACTOR
import ru.rpuxa.strategy.field.interfaces.PlayerBuildings
import ru.rpuxa.strategy.players.Player
import ru.rpuxa.strategy.visual.view.TextureBank

class Town(override var x: Int, override var y: Int, override var owner: Player) : PlayerBuildings {
    val selectionTerritory = 2
    override val icon = TextureBank.TOWN
    override val name = "Город"
    override val description = "Здесь вы можете создавать юниты, строить здания"
    val maxWorkPoints = 500

    var workPoints = 0
    var bought = false
    var level = 1
        private set(value) {
            field = value
        }
    val performance: Int
        get() = (
                INITIAL_PERFORMANCE *
                        Math.pow(TOWN_LEVEL_PERFORMANCE_FACTOR.toDouble(), level.toDouble()) *
                        Math.pow(TOWN_COUNT_PERFORMANCE_FACTOR.toDouble(), owner.townsCount.toDouble())
                ).toInt()

    private var age = 0

    fun addDay() {
        if (++age >= level * (level + 1) / 2) {
            age = 0
            level++
        }
    }
}


