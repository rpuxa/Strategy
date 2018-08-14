package ru.rpuxa.strategy.visual

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Unit
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

class Region(val border: Path = Path())

class RegionList : ArrayList<Region>()

class RegionBuilder(val fieldView: FieldView, val field: Field) {

    fun createFromCells(cells: Collection<Cell>): RegionPaint {
        val lines = LinkedList<Line>()
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

                lines.add(line)
            }
        }
        val regions = RegionList()
        while (lines.isNotEmpty()) {
            var last = lines.removeAt(0)
            val region = Region()
            val start = last.from
            region.border.moveTo(last.from.x, last.from.y)
            region.border.lineTo(last.to.x, last.to.y)

            while (true) {
                val find = lines.find { abs(it.from.x - last.to.x) <= 1 && abs(it.from.y - last.to.y) <= 1 }
                        ?: throw IllegalStateException("path doesn't circle!")
                lines.remove(find)
                region.border.lineTo(find.to.x, find.to.y)
                if (abs(find.to.x - start.x) <= 1 && abs(find.to.y - start.y) <= 1)
                    break
                last = find
            }
            region.border.close()
            regions.add(region)
        }

        return RegionPaint(regions)
    }

    fun createFromTerritories(): Array<RegionPaint> {
        val list = LinkedList<Cell>()
        for ((_, _, cell) in field) {
            list.add(cell)
        }

        val regions = ArrayList<RegionPaint>()
        while (list.isNotEmpty()) {
            val color = list.first.color
            val cells = list.filter { it.color == color }
            list.removeAll(cells)
            val paint = createFromCells(cells)
                    .board(TERRITORY_COLOR_BOARD)
                    .boardWidth(BOARD_TERRITORY_WIDTH)
                    .effects(BOARD_TERRITORY_EFFECTS)
                    .fill(color)
            if (color == NO_PLAYER_COLOR_CELL)
                paint.board(COLOR_NONE)
            regions.add(paint)
        }

        return regions.toTypedArray()
    }

    fun createFromUnitMove(unit: Unit): RegionPaint {
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

        val extract = createFromCells(step(field[unit.x, unit.y])).list
        if (extract.isEmpty() || extract.size > 1)
            throw IllegalStateException("Move selection cannot have more then 1 region")
        return RegionPaint(extract)
    }

    fun createFromTownTerritory(town: Town): RegionPaint {
        val maxDepth = town.selectionTerritory
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

        val extract = createFromCells(step(field[town.x, town.y], 0)).list
        if (extract.isEmpty() || extract.size > 1)
            throw IllegalStateException("Territory selection cannot have more then 1 region")

        return RegionPaint(extract)
    }
}

class RegionPaint(val list: RegionList) {
    private var colorFill = COLOR_NONE
    private var colorBorder = COLOR_NONE
    private var lineEffects: PathEffect? = null
    private var strokeWidth = 3f

    fun fill(colorFill: Int): RegionPaint {
        this.colorFill = colorFill
        return this
    }

    fun board(colorBorder: Int): RegionPaint {
        this.colorBorder = colorBorder
        return this
    }

    fun boardWidth(w: Float): RegionPaint {
        this.strokeWidth = w
        return this
    }

    fun effects(vararg effects: PathEffect): RegionPaint {
        lineEffects = composeEffects(effects)
        return this
    }

    fun paintFill(canvas: Canvas, paint: Paint) {
        if (colorFill != COLOR_NONE) {
            paint.pathEffect = null
            paint.color = colorFill
            paint.style = Paint.Style.FILL

            list.forEach {
                canvas.drawPath(it.border, paint)
            }
        }
    }

    fun paintBorder(canvas: Canvas, paint: Paint) {
        if (colorBorder != COLOR_NONE) {
            paint.pathEffect = lineEffects
            paint.style = Paint.Style.STROKE
            paint.color = colorBorder
            paint.strokeWidth = strokeWidth

            list.forEach {
                canvas.drawPath(it.border, paint)
            }
        }
    }
}

