package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.FieldObjectInfo
import ru.rpuxa.strategy.core.interfaces.field.info.statics.StaticObjectInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject

/**
 * Статичный объект на поле, который
 * не может изменять своего расположения
 */
interface StaticObject : FieldObject {

    override val info: StaticObjectInfo

    /**
     * Может ли юнит [Unit] пройти через этот объект
     *
     * Например горы не являются проходимым объектом
     */
    val passable: Boolean
        get() = info.passable

    override fun copy(): StaticObject
}