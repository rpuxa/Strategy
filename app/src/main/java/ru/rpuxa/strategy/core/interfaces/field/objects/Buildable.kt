package ru.rpuxa.strategy.core.interfaces.field.objects

/**
 * Все объекты поля, реализующий этот интерфейс, можно
 * построить или нанять в городе
 */
interface Buildable : FieldObject {
    /**
     * Цена постройки
     */
    val cost: Int
}