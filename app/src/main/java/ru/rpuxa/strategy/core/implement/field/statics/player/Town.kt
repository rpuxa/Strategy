package ru.rpuxa.strategy.core.implement.field.statics.player

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.PlayerBuildings
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.INITIAL_PERFORMANCE
import ru.rpuxa.strategy.core.others.TOWN_COUNT_PERFORMANCE_FACTOR
import ru.rpuxa.strategy.core.others.TOWN_LEVEL_PERFORMANCE_FACTOR

/**
 * Класс города. Предназначен для постройки новых объектов [Buildable]
 */
class Town(override var x: Int, override var y: Int, override var owner: Player) : PlayerBuildings {
    override val icon = TexturesId.TOWN
    override val name = "Город"
    override val description = "Здесь вы можете создавать юниты, строить здания"

    /**
     * Максимальное количество очков работы, которое может вмещать город
     */
    val maxWorkPoints = 500

    /**
     * Радиус области, которую выделяет город вокруг себя
     */
    val selectionTerritory = 2

    /**
     * Текущее значение очков работы
     */
    var workPoints = 0

    /**
     * true - если в городе, что то построили [Buildable]
     *
     * Обнуляется на новом ходу
     *
     * Город не может строить несколько [Buildable] за один ход
     */
    var bought = false

    /**
     * Уровень города. Влияет на его производительность [performance]
     */
    var level = 1
        private set(value) {
            field = value
        }

    /**
     * Производительность показывает, сколько очков работы прибавляется каждый ход
     */
    val performance: Int
        get() = (
                INITIAL_PERFORMANCE *
                        Math.pow(TOWN_LEVEL_PERFORMANCE_FACTOR.toDouble(), level.toDouble()) *
                        Math.pow(TOWN_COUNT_PERFORMANCE_FACTOR.toDouble(), owner.townsCount.toDouble())
                ).toInt()

    private var daysWithoutNewLevel = 0

    /**
     * Вызывается после завершения хода.
     * Обновляет уровень города, если это нужно
     */
    fun addDay() {
        if (++daysWithoutNewLevel >= level * (level + 1) / 2) {
            daysWithoutNewLevel = 0
            level++
        }
    }
}


