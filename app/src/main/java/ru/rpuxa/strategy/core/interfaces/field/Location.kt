package ru.rpuxa.strategy.core.interfaces.field

/**
 * Интерфейс для всех объектов, которые могут располагаться на поле
 * или имеют свои координаты
 */
interface Location {

    /**
     *  Координата X
     */
    var x: Int

    /**
     *  Координата Y
     */
    var y: Int
}