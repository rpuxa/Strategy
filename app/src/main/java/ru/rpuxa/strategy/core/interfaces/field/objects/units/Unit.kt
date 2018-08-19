package ru.rpuxa.strategy.core.interfaces.field.objects.units

import ru.rpuxa.strategy.core.interfaces.field.objects.Buildable
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject

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
}