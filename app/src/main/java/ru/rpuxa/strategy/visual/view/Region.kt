package ru.rpuxa.strategy.visual.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.interfaces.Field
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.geometry.Line
import ru.rpuxa.strategy.visual.FieldVisualizer
import java.lang.Math.PI
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Регион - несколько клеток на поле, например территория игрока.
 * Может выделятся другим цветом
 */
class Region(
        /**
         * Граница региона, проходящая по его периметру
         */
        val border: Path = Path()
)

/**
 * Список регионов
 *
 * [cells] - коллекция клеток, который содержат все регионы вместе
 */
class RegionList(val cells: Collection<Cell>) : ArrayList<Region>()

/**
 * Строитель регионов
 */
class RegionBuilder(val visual: FieldVisualizer, val field: Field) {

    /**
     *  Создать регион из коллекции клеток
     */
    fun createFromCells(cells: Collection<Cell>): RegionPaint {
        val lines = LinkedList<Line>()
        for (cell in cells) {
            val (worldCellX, worldCellY) = visual.locationToWorld(cell)
            for ((side, neighbour) in field.getNeighbours(cell).withIndex()) {
                if (neighbour in cells || cell == Cell.NONE)
                    continue

                val angle0 = side * PI.toFloat() / 3
                val angle1 = (side + 1) * PI.toFloat() / 3

                val line = worldCellX + CELL_RADIUS * sin(angle0) pt worldCellY + CELL_RADIUS * cos(angle0) line
                        (worldCellX + CELL_RADIUS * sin(angle1) pt worldCellY + CELL_RADIUS * cos(angle1))

                lines.add(line)
            }
        }
        val regions = RegionList(cells)
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

    /**
     * Создать регионы из территорий игроков
     */
    fun createFromTerritories(): Array<RegionPaint> {
        val list = LinkedList<Cell>()
        for (cell in field) {
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

    /**
     * Создать регион из возможных ходов юнита
     */
    fun createFromUnitMove(unit: Unit): RegionPaint {
        val extract = createFromCells(field.getUnitMoves(unit).map { it.cell }).list
        return RegionPaint(extract)
    }
}


/**
 * Класс для отрисовки регионов
 */
class RegionPaint(val list: RegionList) {
    private var colorFill = COLOR_NONE
    private var colorBorder = COLOR_NONE
    private var lineEffects: PathEffect? = null
    private var strokeWidth = 3f
    private var wide = false

    operator fun contains(cell: Cell) = cell in list.cells

    /**
     * Заполнить регион цветом [colorFill]
     */
    fun fill(colorFill: Int): RegionPaint {
        this.colorFill = colorFill
        return this
    }

    /**
     * Установить цвет границы региона
     */
    fun board(colorBorder: Int): RegionPaint {
        this.colorBorder = colorBorder
        return this
    }

    /**
     * Установить ширину региона
     */
    fun boardWidth(w: Float): RegionPaint {
        this.strokeWidth = w
        return this
    }

    /**
     * Установить эффекты для границы региона
     */
    fun effects(vararg effects: PathEffect): RegionPaint {
        lineEffects = composeEffects(effects)
        return this
    }

    /**
     * Установить|снять сетку
     */
    fun wide(on: Boolean = true): RegionPaint {
        wide = on
        return this
    }

    /**
     * Отрисовать регион без его границы
     */
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

    /**
     * Отрисовать регион без его заполнения цветом
     */
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

