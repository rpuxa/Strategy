package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.field.interfaces.StaticObject

/**
 * Синглтон для создания шаблонов полей разной формы
 */
object GameFieldCreator {

    /**
     * Создание прямоугольного поля
     */
    fun rectangle(width: Int, height: Int): HexagonField {
        val field = Array(width) { x -> Array(height) { y -> Cell(StaticObject.EMPTY, x = x, y = y) } }
        return HexagonField(field)
    }
}