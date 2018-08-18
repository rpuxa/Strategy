package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.Move
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.visual.view.RegionPaint

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
            if (cell.canStop && cell != startCell && cells.find { it.cell == cell && it.steps <= depth } == null) {
                for (move in cells)
                    if (move.cell == cell) {
                        cells.remove(move)
                        break
                    }
                cells.add(Move(cell, depth))
            }
            if (depth == maxDepth)
                return
            val neighbours = getNeighbours(cell).filter { it != startCell && it.canPass }
            for (n in neighbours)
                step(n, depth + 1)
        }
        step(startCell)

        return cells
    }

    fun getTownTerritory(town: Town): ArrayList<Cell> {
        val maxDepth = town.selectionTerritory
        val cells = ArrayList<Cell>()
        fun step(cell: Cell, depth: Int) {
            if (cell !in cells)
                cells.add(cell)
            if (depth == maxDepth) {
                return
            }
            for (n in getNeighbours(cell))
                step(n, depth + 1)
        }

        step(this[town], 0)
        return cells
    }
}
