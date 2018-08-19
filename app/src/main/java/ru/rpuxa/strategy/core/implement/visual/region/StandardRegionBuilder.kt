package ru.rpuxa.strategy.core.implement.visual.region

import ru.rpuxa.strategy.android.visual.region.PathRegion
import ru.rpuxa.strategy.core.geometry.Line
import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionBuilder
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint
import ru.rpuxa.strategy.core.others.CELL_NONE
import ru.rpuxa.strategy.core.others.CELL_RADIUS
import ru.rpuxa.strategy.core.others.line
import ru.rpuxa.strategy.core.others.pt
import java.util.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class StandardRegionBuilder(override val visual: FieldVisualizer, override val field: Field) : RegionBuilder{

    override fun createFromCells(cells: Collection<Cell>): RegionPaint {
        val lines = LinkedList<Line>()
        for (cell in cells) {
            val (worldCellX, worldCellY) = visual.locationToWorld(cell)
            for ((side, neighbour) in field.getNeighbours(cell).withIndex()) {
                if (neighbour in cells || cell == CELL_NONE)
                    continue

                val angle0 = side * Math.PI.toFloat() / 3
                val angle1 = (side + 1) * Math.PI.toFloat() / 3

                val line = worldCellX + CELL_RADIUS * sin(angle0) pt worldCellY + CELL_RADIUS * cos(angle0) line
                        (worldCellX + CELL_RADIUS * sin(angle1) pt worldCellY + CELL_RADIUS * cos(angle1))

                lines.add(line)
            }
        }
        val regions = ArrayRegionList(cells)
        while (lines.isNotEmpty()) {
            var last = lines.removeAt(0)
            val region = PathRegion()
            val start = last.from
            region.moveTo(last.from.x, last.from.y)
            region.lineTo(last.to.x, last.to.y)

            while (true) {
                val find = lines.find { abs(it.from.x - last.to.x) <= 1 && abs(it.from.y - last.to.y) <= 1 }
                        ?: throw IllegalStateException("path doesn't circle!")
                lines.remove(find)
                region.lineTo(find.to.x, find.to.y)
                if (abs(find.to.x - start.x) <= 1 && abs(find.to.y - start.y) <= 1)
                    break
                last = find
            }
            region.close()
            regions.add(region)
        }

        return StandardRegionPaint(regions)
    }

    override fun createFromUnitMove(unit: Unit): RegionPaint {
        val extract = createFromCells(field.getUnitMoves(unit).map { it.cell }).list
        return StandardRegionPaint(extract)
    }
}