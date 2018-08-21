package ru.rpuxa.strategy.core.implement.field

import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.MutableField
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.others.CELL_NONE
import ru.rpuxa.strategy.core.others.Copyable
import ru.rpuxa.strategy.core.others.UNIT_NONE


/**
 * Гексагональное поле игры
 */
class HexagonField(private val field: Array<Array<Cell>>) : MutableField {

    companion object {
        private val neighboursEven = arrayOf(
                1 to 0,
                2 to 0,
                1 to -1,
                -1 to -1,
                -2 to 0,
                -1 to 0
        )

        private val neighborsOdd = arrayOf(
                1 to 1,
                2 to 0,
                1 to 0,
                -1 to 0,
                -2 to 0,
                -1 to 1
        )
    }

    override fun get(x: Int, y: Int) =
            if (!bound(x, y))
                CELL_NONE
            else
                field[x][y]

    override fun set(x: Int, y: Int, value: Cell) {
        field[x][y] = value
    }

    private fun bound(x: Int, y: Int) = field.isNotEmpty() && x >= 0 && y >= 0 && field.size > x && field[0].size > y

    override val iterator: Iterator<Cell>
        get() = object : Iterator<Cell> {
            var i = 0
            val sizeX = this@HexagonField.field.size
            val sizeY = this@HexagonField.field[0].size
            val size = sizeX * sizeY

            override fun hasNext() = i < size

            override fun next(): Cell {
                val index = i++
                return this@HexagonField[index % sizeX, index / sizeX]
            }
        }


    override fun getNeighbours(x: Int, y: Int): Array<Cell> {
        val neighboursOffers = if (x % 2 == 0) neighboursEven else neighborsOdd
        return Array(neighboursOffers.size) {
            val (shiftX, shiftY) = neighboursOffers[it]
            this[x + shiftX, y + shiftY]
        }
    }

    override fun changeLocationUnit(unit: Unit, location: Location) {
        this[unit].unit = UNIT_NONE
        unit.x = location.x
        unit.y = location.y
        this[unit].unit = unit
    }

    override fun copy(): HexagonField = HexagonField(
            Array(field.size) {x ->
                Array(field[0].size) { y ->
                    field[x][y].copy()
                }
            }
    )
}

