package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject

/**
 * Статичный объект на поле, который
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