package ru.rpuxa.strategy.visual

/**
 * Интерфейс для визуализации анимаций.
 * Все анимации должны анимироваться асинхронно.
 */
interface FieldAnimator {

    /**
     * Анимировать [animation]
     */
    fun animate(animation: Animation)
}