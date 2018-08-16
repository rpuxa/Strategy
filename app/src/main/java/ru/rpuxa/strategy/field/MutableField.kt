package ru.rpuxa.strategy.field

interface MutableField : Field {

    operator fun set(x: Int, y: Int, value: Cell)

    fun changeLocationUnit(unit: Unit, location: Location)
}