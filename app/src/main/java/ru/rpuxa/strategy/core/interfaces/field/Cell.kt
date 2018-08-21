package ru.rpuxa.strategy.core.interfaces.field

import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player

interface Cell : Location {

    /**
     *  Статический объект, которых находится на данной клетке
     *
     *  [STATIC_OBJECT_NONE] - для обозначения отсутствия
     */
    var staticObject: StaticObject

    /**
     *  Юнит, который находится на этой клетке
     *
     *  [UNIT_NONE] - для обозначения отсутствия
     */
    var unit: Unit

    /**
     *  Игрок, владеющий клеткой
     *
     *  [PLAYER_NONE] - для обозначения отсутствия
     */
    var owner: Player

    /**
     * Цвет клетки
     */
    val color: Int

    /**
     * Может ли юнит пройти мимо данной клетки (не останавливаясь)
     */
    val canPass: Boolean

    /**
     * Может ли юнит остановится в данной клетке
     */
    val canStop: Boolean

    val objects
        get() = arrayOf(staticObject, unit)

    override fun copy(): Cell
}