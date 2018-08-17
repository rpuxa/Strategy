package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Location

interface MutableField : Field {

    operator fun set(x: Int, y: Int, value: Cell)

    fun changeLocationUnit(unit: Unit, location: Location)
}