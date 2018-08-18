package ru.rpuxa.strategy.field.interfaces

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