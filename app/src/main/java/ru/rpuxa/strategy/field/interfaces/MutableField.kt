package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Location

/**
 * Интерфейс расширяющий возможности поля
 *
 * Добавляет методы записи
 */
interface MutableField : Field {

    /**
     * Установить клетку по данным координатам
     */
    operator fun set(x: Int, y: Int, value: Cell)

    /**
     * Изменить расположение юнита [unit], на [location]
     */
    fun changeLocationUnit(unit: Unit, location: Location)
}