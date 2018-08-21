package ru.rpuxa.strategy.core.interfaces.field.objects.units

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.units.UnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.game.Player

/**
 * Юнит. Может перемещаться по полю
 */
interface Unit : BuildableObject {
    override val info: UnitInfo

    /**
     * Текущее здоровье юнита
     * При достижении 0 юнит умирает
     */
    var health: Int

    /**
     *  Сколько клеток может пройти юнит.
     *  Восстанавливаются при начале нового хода
     */
    var movePoints: Int

    /**
     * Начальное количество [movePoints]
     */
    val baseMovePoints: Int
        get() = info.baseMovePoints

    val baseHealth: Int
        get() = info.baseHealth

    /**
     * Владелец юнита
     */
    val owner: Player

    /**
     * Возвращает количество оставшихся жизней после
     * боя с юнитом [enemy]
     */
    fun fight(enemy: Unit): Int

    override fun copy(): Unit
}