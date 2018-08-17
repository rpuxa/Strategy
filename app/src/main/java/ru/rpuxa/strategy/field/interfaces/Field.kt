package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.Move

interface Field : Iterable<Cell> {
    val iterator: Iterator<Cell>

    override fun iterator() = iterator

    operator fun get(x: Int, y: Int): Cell

    operator fun get(location: Location) = get(location.x, location.y)

    fun getNeighbours(x: Int, y: Int): Array<Cell>

    fun getNeighbours(location: Location) = getNeighbours(location.x, location.y)

    fun getUnitMoves(unit: Unit): ArrayList<Move> {
        val maxDepth = unit.movePoints
        val startCell = this[unit]
        val cells = ArrayList<Move>()
        fun step(cell: Cell, depth: Int = 0) {
            if ((depth != maxDepth || cell.canStop) && cell != startCell && cells.find { it.cell == cell } == null)
                cells.add(Move(cell, depth))
            if (depth == maxDepth)
                return
            val neighbours = getNeighbours(cell).filter { it != startCell && it.canPass }
            for (n in neighbours)
                step(n, depth + 1)
        }
        step(startCell)

        return cells
    }
}