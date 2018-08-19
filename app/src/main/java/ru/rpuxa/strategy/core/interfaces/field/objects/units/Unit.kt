package ru.rpuxa.strategy.core.interfaces.field.objects.units

import ru.rpuxa.strategy.core.interfaces.field.objects.Buildable
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.game.Player

/**
 * Юнит. Может перемещаться по полю
 */
interface Unit : FieldObject, Buildable {

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
    val maxMovePoints: Int

    /**
     * Владелец юнита
     */
    val owner: Player

    /**
     * Возвращает количество оставшихся жизней после
     * боя с юнитом [enemy]
     */
    fun fight(enemy: Unit): Int
}