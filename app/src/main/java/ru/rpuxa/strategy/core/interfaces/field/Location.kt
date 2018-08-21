package ru.rpuxa.strategy.core.interfaces.field

import ru.rpuxa.strategy.core.others.Copyable

/**
 * Интерфейс для всех объектов, которые могут располагаться на поле
 * или имеют свои координаты
 */
interface Location : Copyable<Location> {

    /**
     *  Координата X
     */
    var x: Int

    /**
     *  Координата Y
     */
    var y: Int
}