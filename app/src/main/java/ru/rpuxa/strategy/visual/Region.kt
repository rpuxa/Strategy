package ru.rpuxa.strategy.visual

import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import ru.rpuxa.strategy.CELL_RADIUS
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.loc
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.geometry.Line
import ru.rpuxa.strategy.geometry.line
import ru.rpuxa.strategy.geometry.pt
import java.lang.Math.PI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

open class Region(val border: Path = Path(), val color: Int)

class Selection(region: Region,val style: Array<out PathEffect>) : Region(region.border, region.color)

class RegionBuilder(val fieldView: FieldView, val field: Field) {

    fun extract(cells: Collection<Cell>, color: Int): Array<Region> {
        class ColorLine(val line: Line, val color: Int)

        val lines = LinkedList<ColorLine>()
        for (cell in cells) {
            val x = cell.x
            val y = cell.y
            val (worldCellX, worldCellY) = fieldView.getCellCoords(x, y)
            for ((side, neighbour) in field.getNeighbours(x, y).withIndex()) {
                if (neighbour in cells || cell == Cell.NONE)
                    continue

                val angle0 = side * PI.toFloat() / 3
                val angle1 = (side + 1) * PI.toFloat() / 3

                val line = worldCellX + CELL_RADIUS * sin(angle0) pt worldCellY + CELL_RADIUS * cos(angle0) line
                        (worldCellX + CELL_RADIUS * sin(angle1) pt worldCellY + CELL_RADIUS * cos(angle1))

                lines.add(ColorLine(line, cell.color))
            }
        }
        val regions = ArrayList<Region>()
        while (lines.isNotEmpty()) {
            var last = lines.removeAt(0)
            val region = Region(color = color)
            val start = last.line.from
            region.border.moveTo(last.line.from.x, last.line.from.y)
            region.border.lineTo(last.line.to.x, last.line.to.y)

            while (true) {
                val find = lines.find { abs(it.line.from.x - last.line.to.x) <= 1 && abs(it.line.from.y - last.line.to.y) <= 1 }
                        ?: throw IllegalStateException("path doesn't circle!")
                lines.remove(find)
                region.border.lineTo(find.line.to.x, find.line.to.y)
                if (abs(find.line.to.x - start.x) <= 1 && abs(find.line.to.y - start.y) <= 1)
                    break
                last = find
            }
            region.border.close()
            regions.add(region)
        }

        return regions.toTypedArray()

    }

    fun extractTerritories(): Array<Region> {
        val list = LinkedList<Cell>()
        for ((_, _, cell) in field) {
            list.add(cell)
        }

        val regions = ArrayList<Region>()
        while (list.isNotEmpty()) {
            val color = list.first.color
            val cells = list.filter { it.color == color }
            list.removeAll(cells)
            regions.addAll(extract(cells, color))
        }

        return regions.toTypedArray()
    }

    inner class SelectionBuilder(val color: Int, vararg val effects: PathEffect) {

        fun moveSelection(unit: Unit): Selection {
            val maxDepth = unit.movePoints

            fun step(cell: Cell, depth: Int = 0): ArrayList<Cell> {
                val list = ArrayList<Cell>()
                if (!cell.canPass)
                    return list
                if (depth == maxDepth) {
                    if (cell.canStop)
                        list.add(cell)
                    return list
                }
                if (depth != 0)
                    list.add(cell)
                for (n in field.getNeighbours(cell).filter { it !in list })
                    list.addAll(step(cell, depth + 1))

                return list
            }

            val extract = extract(step(field[unit.x, unit.y]), color)
            if (extract.isEmpty() || extract.size > 1)
                throw IllegalStateException("Move selection cannot have more then 1 region")
            return Selection(extract[0], effects)
        }

        fun territorySelection(town: Town): Selection {
            val maxDepth = town.selectionTerretory
            fun step(cell: Cell, depth: Int): ArrayList<Cell> {
                val list = ArrayList<Cell>()
                if (depth == maxDepth) {
                    list.add(cell)
                    return list
                }
                for (n in field.getNeighbours(cell).filter { it !in list })
                    list.addAll(step(cell, depth + 1))

                return list
            }

            val extract = extract(step(field[town.x, town.y], 0), color)
                    if (extract.isEmpty() || extract.size > 1)
                        throw IllegalStateException("Territory selection cannot have more then 1 region")

            return Selection(extract[0], effects)
        }
    }
}