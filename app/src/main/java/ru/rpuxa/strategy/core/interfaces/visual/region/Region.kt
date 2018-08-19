package ru.rpuxa.strategy.core.interfaces.visual.region

/**
 * Регион - несколько клеток на поле, например территория игрока.
 * Может выделятся другим цветом
 */
interface Region {

    fun moveTo(x: Float, y: Float)

    fun lineTo(x: Float, y: Float)

    fun close()
}