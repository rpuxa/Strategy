package ru.rpuxa.strategy.core.interfaces.field

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.others.UNIT_NONE

/**
 *  Read-only Интерфейс для поля игры, который
 *  хранит все клетки.
 *  Может быть проитерирован.
 *  Не имеет методов для изменения @see [MutableField]
 */
interface   Field : Iterable<Cell> {
    /**
     * Итератор из интерфейса Iterable
     * Для удобства переделан в свойство
     */
    val iterator: Iterator<Cell>
    override fun iterator() = iterator

    /**
     *  Получить клетку по координатам или локации
     *  Для удобства является оператором
     */
    operator fun get(x: Int, y: Int): Cell
    operator fun get(location: Location) = get(location.x, location.y)

    /**
     * Получить соседей клетки, расположенной по координатам или локации
     */
    fun getNeighbours(x: Int, y: Int): Array<Cell>
    fun getNeighbours(location: Location) = getNeighbours(location.x, location.y)

    /**
     *  Получить клетки на которые может сходить юнит
     */
    fun getUnitMoves(unit: Unit): ArrayList<UnitMove> {
        val maxDepth = unit.movePoints
        val startCell = this[unit]
        val cells = ArrayList<UnitMove>()
        fun step(cell: Cell, depth: Int = 0) {
            if ((cell.canStop || cell.unit != UNIT_NONE && cell.unit.owner != unit.owner) &&
                    cell != startCell &&
                    cells.find { it.cell == cell && it.steps <= depth } == null) {
                for (move in cells)
                    if (move.cell == cell) {
                        cells.remove(move)
                        break
                    }
                if (cell.unit == UNIT_NONE || unit is FightingUnit)
                cells.add(UnitMove(cell, depth))
            }
            if (depth == maxDepth || cell.unit != UNIT_NONE)
                return
            val neighbours = getNeighbours(cell).filter { it != startCell && it.canPass }
            for (n in neighbours)
                step(n, depth + 1)
        }
        step(startCell)

        return cells
    }

    /**
     * Получить все клетки, которые принадлежат городу
     */
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

    /**
     * Data класс для хранения возможных ходов юнита
     */
    data class UnitMove(
            /**
             * Клетка, куда он может добраться
             */
            val cell: Cell,

            /**
             * Сколько ОП ему нужно потратить для перемещения
             * на эту клетку
             */
            val steps: Int
    )
}
