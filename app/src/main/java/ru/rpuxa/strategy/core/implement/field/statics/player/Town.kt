package ru.rpuxa.strategy.core.implement.field.statics.player

import ru.rpuxa.strategy.core.implement.field.info.statics.player.TownInfo
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.PlayerBuilding
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.INITIAL_PERFORMANCE
import ru.rpuxa.strategy.core.others.TOWN_COUNT_PERFORMANCE_FACTOR
import ru.rpuxa.strategy.core.others.TOWN_LEVEL_PERFORMANCE_FACTOR
import ru.rpuxa.strategy.core.others.copyLocation

/**
 * Класс города. Предназначен для постройки новых объектов [Buildable]
 */
class Town : PlayerBuilding {

    constructor(location: Location, owner: Player) : super() {
        this.owner = owner
        x = location.x
        y = location.y
        workPoints = 0
        bought = false
        level = 1
        movesToDestroy = -1
    }

    constructor(location: Location, owner: Player, id: Long, workPoints: Int,
                bought: Boolean, level: Int, movesToDestroy: Int) : this(location, owner) {
        super.id = id
        this.workPoints = workPoints
        this.bought = bought
        this.level = level
        this.movesToDestroy = movesToDestroy
    }


    override val info
        get() = TownInfo

    override var owner: Player

    override var x: Int
    override var y: Int

    override fun copy(): Town = Town(this.copyLocation(), owner, id, workPoints, bought, level, movesToDestroy)

    /**
     * Текущее значение очков работы
     */
    var workPoints: Int

    val maxWorkPoints: Int
        get() = info.maxWorkPoints

    val selectionTerritory: Int
        get() = info.selectionTerritory

    /**
     * true - если в городе, что то построили [Buildable]
     *
     * Обнуляется на новом ходу
     *
     * Город не может строить несколько [Buildable] за один ход
     */
    var bought: Boolean

    /**
     * Уровень города. Влияет на его производительность [performance]
     */
    var level: Int
        private set(value) {
            field = value
        }

    var movesToDestroy: Int

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


