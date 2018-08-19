package ru.rpuxa.strategy.core.implement.builders

import ru.rpuxa.strategy.core.implement.field.CreateCell
import ru.rpuxa.strategy.core.implement.field.HexagonField
import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.others.STATIC_OBJECT_NONE

/**
 * Строитель для создания шаблонов полей разной формы
 */
object FieldBuilder {

    /**
     * Создание прямоугольного поля
     */
    fun rectangle(width: Int, height: Int): HexagonField {
        val field = Array(width) { x -> Array<Cell>(height) { y -> CreateCell(STATIC_OBJECT_NONE, x = x, y = y) } }
        return HexagonField(field)
    }
}