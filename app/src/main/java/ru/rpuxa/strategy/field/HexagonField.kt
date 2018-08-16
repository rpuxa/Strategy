package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.field.interfaces.NaturalStructures

typealias Matrix<T> = Array<Array<T>>

val neighboursEven = arrayOf(
        1 to 0,
        2 to 0,
        1 to -1,
        -1 to -1,
        -2 to 0,
        -1 to 0
)

val neighborsOdd = arrayOf(
        1 to 1,
        2 to 0,
        1 to 0,
        -1 to 0,
        -2 to 0,
        -1 to 1
)

/**
 * Гексагональное поле игры
 */
class HexagonField(private val field: Matrix<Cell>) : MutableField {

    override fun get(x: Int, y: Int) = if (!bound(x, y))
        Cell.NONE
    else field[x][y]

    override fun set(x: Int, y: Int, value: Cell) {
        field[x][y] = value
    }

    private fun bound(x: Int, y: Int) = field.isNotEmpty() && x >= 0 && y >= 0 && field.size > x && field[0].size > y

    override val iterator: Iterator<Cell>
        get() = object : Iterator<Cell> {
            var i = 0
            override fun hasNext() = i < this@HexagonField.field.size * this@HexagonField.field[0].size

            override fun next(): Cell {
                val size = this@HexagonField.field.size
                val x = i % size
                val y = i / size
                i++
                return this@HexagonField[x, y]
            }
        }


    @Suppress("UNCHECKED_CAST")
    override fun getNeighbours(x: Int, y: Int): Array<Cell> {
        val neighboursOffers = if (x % 2 == 0) neighboursEven else neighborsOdd
        val array = arrayOfNulls<Cell>(neighboursOffers.size)
        for (i in neighboursOffers.indices) {
            val (shiftX, shiftY) = neighboursOffers[i]
            array[i] = get(x + shiftX, y + shiftY)
        }

        return array as Array<Cell>
    }

    override fun changeLocationUnit(unit: Unit, location: Location) {
        this[unit].unit = Unit.NONE
        unit.x = location.x
        unit.y = location.y
        this[unit].unit = unit
    }

}

object GameBoardCreator {

    fun square(width: Int, height: Int): HexagonField {
        val field = Array(width) { x -> Array(height) { y -> Cell(NaturalStructures.EMPTY, x = x, y = y) } }
        return HexagonField(field)
    }
}