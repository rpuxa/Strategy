package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.field.objects.Cell

typealias Matrix<T> = Array<Array<T>>

/**
 * Гексагональное поле игры
 */
class HexagonField(field: Matrix<Cell>) : HexagonMatrix<Cell>(field), Field {
    @Suppress("UNCHECKED_CAST")
    override fun getNeighbours(x: Int, y: Int) = super.getNeighbours(x, y, arrayOfNulls(offers.size)) as Array<Cell>

    override val iterator: Iterator<Triple<Int, Int, Cell>>
        get() = object : Iterator<Triple<Int, Int, Cell>> {
            var i = 0
            override fun hasNext() = i < this@HexagonField.field.size * this@HexagonField.field[0].size

            override fun next(): Triple<Int, Int, Cell> {
                val size = this@HexagonField.field.size
                val x = i % size
                val y = i / size
                i++
                return Triple(x, y, this@HexagonField[x, y])
            }
        }

}

object GameBoardCreator {

    fun square(width: Int, height: Int): HexagonField {
        val field = Array(width) { _ -> Array(height) { Cell(NaturalStructures.EMPTY) } }
        return HexagonField(field)
    }
}