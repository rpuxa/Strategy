package ru.rpuxa.strategy.field

data class CellLocation(override val x: Int, override val y: Int, val cell: Cell) : Location

infix fun Location.addCell(cell: Cell) = CellLocation(x, y, cell)