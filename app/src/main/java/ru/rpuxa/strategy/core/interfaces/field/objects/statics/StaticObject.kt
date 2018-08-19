package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject

/**
 * Статический объект на поле, которые
 * не может изменять своего расположения
 */
interface StaticObject : FieldObject {
    /**
     * Может ли юнит [Unit] пройти через этот объект
     *
     * Например горы не являются проходимым объектом
     */
    val passable: Boolean
}