package ru.rpuxa.strategy.core.interfaces.field

import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit

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

    fun setUnit(unit: Unit) {
        this[unit].unit = unit
    }
}