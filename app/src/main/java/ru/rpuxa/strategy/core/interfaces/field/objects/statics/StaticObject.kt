package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.info.statics.StaticObjectInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject

/**
 * Статичный объект на поле, который
 * не может изменять своего расположения
 */
abstract class StaticObject : FieldObject() {

    abstract override val info: StaticObjectInfo

    /**
     * Может ли юнит [Unit] пройти через этот объект
     *
     * Например горы не являются проходимым объектом
     */
    open val passable: Boolean
        get() = info.passable

    abstract override fun copy(): StaticObject
}