package ru.rpuxa.strategy.core.interfaces.visual.region


import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.others.*
import java.util.*

interface RegionBuilder {

    val visual: FieldVisualizer

    var field: Field

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
                    .boardEffects(BOARD_TERRITORY_EFFECTS)
                    .fillEffects(TERRITORY_EFFECTS)
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
    fun createFromUnitMove(unit: Unit): Array<RegionPaint> {
        val cells = field.getUnitMoves(unit).map { it.cell }
        val moves = createFromCells(cells.filter { it.unit == UNIT_NONE })
                .board(UNIT_REGION_MOVE_BORDER_COLOR)
                .boardEffects(UNIT_REGION_MOVE_BORDER_EFFECTS)
                .boardWidth(UNIT_REGION_MOVE_BORDER_WIDTH)

        val attacks = createFromCells(cells.filter { it.unit != UNIT_NONE })
                .board(UNIT_REGION_ATTACK_BORDER_COLOR)
                .boardEffects(UNIT_REGION_MOVE_BORDER_EFFECTS)
                .boardWidth(UNIT_REGION_MOVE_BORDER_WIDTH)
        return arrayOf(moves, attacks)
    }
}