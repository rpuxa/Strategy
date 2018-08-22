package ru.rpuxa.strategy.core.interfaces.field.objects

import ru.rpuxa.strategy.core.interfaces.field.info.BuildableInfo

/**
 * Все объекты поля, реализующий этот интерфейс, можно
 * построить или нанять в городе
 */
abstract class BuildableObject : FieldObject() {
    abstract override val info: BuildableInfo

    /**
     * Цена постройки
     */
    val cost: Int
        get() = info.cost
}