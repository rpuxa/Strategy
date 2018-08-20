package ru.rpuxa.strategy.core.interfaces.visual

/**
 * Интерфейс анимации
 */
interface Animation {

    /**
     * Длительность в millis
     */
    val duration: Int

    val async: Boolean
}