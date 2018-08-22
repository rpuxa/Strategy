package ru.rpuxa.strategy.core.interfaces.field.objects.units

import ru.rpuxa.strategy.core.interfaces.field.Owned
import ru.rpuxa.strategy.core.interfaces.field.info.units.UnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject

/**
 * Юнит. Может перемещаться по полю
 */
abstract class Unit : BuildableObject(), Owned {
    abstract override val info: UnitInfo

    /**
     * Текущее здоровье юнита
     * При достижении 0 юнит умирает
     */
    abstract var health: Int

    /**
     *  Сколько клеток может пройти юнит.
     *  Восстанавливаются при начале нового хода
     */
    abstract var movePoints: Int

    /**
     * Начальное количество [movePoints]
     */
    val baseMovePoints: Int
        get() = info.baseMovePoints

    val baseHealth: Int
        get() = info.baseHealth

    /**
     * Возвращает количество оставшихся жизней после
     * боя с юнитом [enemy]
     */
    abstract fun fight(enemy: Unit): Int

    abstract override fun copy(): Unit
}