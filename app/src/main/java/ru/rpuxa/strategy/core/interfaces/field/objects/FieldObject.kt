package ru.rpuxa.strategy.core.interfaces.field.objects

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.FieldObjectInfo

/**
 * Интерфейс объекта на поле [Field], таких как юниты [Unit]
 * горы или здания [StaticObject]
 */
interface FieldObject : Location {
    val info: FieldObjectInfo

    /**
     * Id иконки объекта
     *
     * id вместо самой иконки выбрано по причине лучшей производительности.
     * Саму иконку по id, можно получить в хранилище текстур
     */

    val icon: Int
        get() = info.icon

    /**
     *  Название объекта
     */
    val name: String
        get() = info.name

    /**
     * Краткое описания
     */
    val description: String
        get() = info.description

}