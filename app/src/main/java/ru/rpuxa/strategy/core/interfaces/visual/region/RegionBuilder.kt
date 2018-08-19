package ru.rpuxa.strategy.core.interfaces.visual.region


import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.others.*
import java.util.*

interface RegionBuilder {

    val visual: FieldVisualizer

    val field: Field

    /**
     *  Создать регион из коллекции клеток
     */
    fun createFromCells(cells: Collection<Cell>): RegionPaint

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
    fun createFromUnitMove(unit: Unit): RegionPaint
}