package ru.rpuxa.strategy.visual

import ru.rpuxa.strategy.field.*
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.objects.player.Town

class Selection(val points: Array<Location>) {

    fun cells(field: Field) = Sequence { field.iterator }
            .filter { toFind ->
                points.find {
                    it.x == toFind.x && it.y == toFind.y
                } != null
            }.toList()

    companion object {
        fun fromFilter(field: Field, filter: (Int, Int, Cell) -> Boolean) =
                Selection(
                        Sequence { field.iterator }
                                .filter { filter(it.x, it.y, it.cell) }
                                .map { it.x loc it.y }
                                .toList()
                                .toTypedArray()
                )

        fun moveSelection(field: Field, unit: Unit): Selection {
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

            return Selection(
                    step(field[unit.x, unit.y])
                            .map { it.x loc it.y }
                            .toTypedArray()
            )
        }

        fun territorySelection(field: Field, town: Town) {
            //TODO рекурсией или нет надо подумать
        }
    }
}