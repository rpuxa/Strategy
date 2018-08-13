package ru.rpuxa.strategy.visual

import android.graphics.Path
import ru.rpuxa.strategy.CELL_RADIUS
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.geometry.Line
import ru.rpuxa.strategy.geometry.line
import ru.rpuxa.strategy.geometry.pt
import java.lang.Math.PI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

data class Region(val border: Path = Path(), val color: Int) {

    companion object {
      /*  fun lines(field: HexagonField, fieldView: FieldView): Array<Line> {
            class ColorLine(val line: Line, val color: Int)

            val lines = ArrayList<ColorLine>()
            for ((x, y, cell) in field) {
                val (worldCellX, worldCellY) = fieldView.getCellCoords(x, y)
                for ((side, neighbour) in field.getNeighbours(x, y).withIndex()) {
                    if (cell.color == neighbour.color || cell == Cell.NONE)
                        continue

                    val angle0 = side * PI.toFloat() / 3
                    val angle1 = (side + 1) * PI.toFloat() / 3

                    val line = worldCellX + CELL_RADIUS * sin(angle0) pt worldCellY + CELL_RADIUS * cos(angle0) line
                            (worldCellX + CELL_RADIUS * sin(angle1) pt worldCellY + CELL_RADIUS * cos(angle1))

                    lines.add(ColorLine(line, cell.color))
                }
            }

            return lines.map { it.line }.toTypedArray()
        }*/

        fun extractRegions(field: HexagonField, fieldView: FieldView, filter: ((Int, Int, Cell) -> Boolean)? = null): Array<Region> {
            class ColorLine(val line: Line, val color: Int)

            val lines = LinkedList<ColorLine>()
            for ((x, y, cell) in field) {
                if (filter != null && !filter(x, y, cell))
                    continue
                val (worldCellX, worldCellY) = fieldView.getCellCoords(x, y)
                for ((side, neighbour) in field.getNeighbours(x, y).withIndex()) {
                    if (cell.color == neighbour.color || cell == Cell.NONE)
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
                val region = Region(color = last.color)
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
    }
}