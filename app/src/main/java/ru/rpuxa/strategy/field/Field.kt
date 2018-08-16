package ru.rpuxa.strategy.field

interface Field : Iterable<Cell> {
    val iterator: Iterator<Cell>

    override fun iterator() = iterator

    operator fun get(x: Int, y: Int): Cell

    operator fun get(location: Location) = get(location.x, location.y)

    fun getNeighbours(x: Int, y: Int): Array<Cell>

    fun getNeighbours(location: Location) = getNeighbours(location.x, location.y)
}