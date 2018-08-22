package ru.rpuxa.strategy.core.interfaces.field.objects

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.FieldObjectInfo
import ru.rpuxa.strategy.core.others.random

/**
 * Интерфейс объекта на поле [Field], таких как юниты [Unit]
 * горы или здания [StaticObject]
 */
abstract class FieldObject : Location {
    abstract val info: FieldObjectInfo

    /**
     * Id иконки объекта
     *
     * id вместо самой иконки выбрано по причине лучшей производительности.
     * Саму иконку по id, можно получить в хранилище текстур
     */


    open val icon: Int
        get() = info.icon


    var id = random.nextLong()

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

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is FieldObject -> false
        else -> other.id == id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + info.hashCode()
        return result
    }


}