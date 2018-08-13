package ru.rpuxa.strategy.field

interface Field : Iterable<CellLocation> {

    val iterator: Iterator<CellLocation>

    override fun iterator() = iterator

    operator fun get(x: Int, y: Int): Cell

    operator fun set(x: Int, y: Int, value: Cell)

    fun getNeighbours(x: Int, y: Int): Array<Cell>

    fun getNeighbours(cell: Cell) = getNeighbours(cell.x, cell.y)
}