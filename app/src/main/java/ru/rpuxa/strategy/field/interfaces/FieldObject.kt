package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Location

/**
 * Интерфейс объекта на поле [Field], таких как юниты [Unit]
 * горы или здания [StaticObject]
 */
interface FieldObject : Location {
    /**
     * Id иконки объекта
     *
     * id вместо самой иконки выбрано по причине лучшей производительности.
     * Саму иконку по id, можно получить в хранилище текстур
     */
    val icon: Int

    /**
     *  Название объекта
     */
    val name: String

    /**
     * Краткое описания
     */
    val description: String
}