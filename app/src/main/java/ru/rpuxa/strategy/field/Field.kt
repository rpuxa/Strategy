package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.field.objects.Cell

interface Field {

    val iterator: Iterator<Triple<Int, Int, Cell>>

    operator fun get(x: Int, y: Int): Cell

    operator fun set(x: Int, y: Int, value: Cell)

    fun getNeighbours(x: Int, y: Int): Array<Cell>

}