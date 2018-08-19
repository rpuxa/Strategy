package ru.rpuxa.strategy.core.interfaces.visual

import ru.rpuxa.strategy.core.geometry.Point
import ru.rpuxa.strategy.core.implement.game.players.Human
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint

/**
 * Интерфейс для визуализации поля. Предназначен для кроссплатформенности, т.к
 * на разных устройствах разные способы визуализации. С помощью него пользователь
 * может взаимодействовать с игрой
 */
interface FieldVisualizer {
    /**
     * Все анимации делаются через это свойство
     */
    val animator: FieldAnimator

    var field: Field

    /**
     * Один из главных методов для отрисовки поля
     */
    fun draw(field: Field)

    /**
     * Отрисовать регион
     */
    fun select(region: RegionPaint)

    /**
     * Убрать регион
     */
    fun deselect(region: RegionPaint)

    /**
     * Перевести координаты поля в мировые координаты
     */
    fun locationToWorld(location: Location) = locationToWorld(location.x, location.y)
    fun locationToWorld(x: Int, y: Int): Point

    /**
     * Определить человека, который будет управлять визуализатором
     */
    fun setControllingHuman(human: Human)

    /**
     * Перевести координаты камеры в мировые координаты
     */
    fun projectToWorld(x: Float, y: Float): Point

    /**
     * Переместить камеру
     */
    fun translateCamera(deltaX: Float, deltaY: Float)

    /**
     * Обновить визуализатор.
     * Обычно выполняет после какого то действия, чтобы
     * оно стало видно пользователю
     */
    fun invalidate()

    /**
     * Увеличить ширину и высоту рамки камеры на [value]
     */
    fun zoomCamera(value: Float)

    fun openInfoHeader(selectedObject: FieldObject)

    fun closeInfoHeader(invalidate: Boolean)
}