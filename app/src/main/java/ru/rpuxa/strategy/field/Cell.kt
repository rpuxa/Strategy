package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.NO_PLAYER_COLOR_CELL
import ru.rpuxa.strategy.NULL_COLOR_CELL
import ru.rpuxa.strategy.field.interfaces.FieldObject
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.field.units.NO_UNIT
import ru.rpuxa.strategy.players.Player


class Cell(var obj: FieldObject,
           val unit: Unit = NO_UNIT,
           var owner: Player = Player.NONE,
           override val x: Int,
           override val y: Int
) : Location {

    val color: Int
        get() = when {
            this === NONE -> NULL_COLOR_CELL
            obj === NaturalStructures.EMPTY -> NO_PLAYER_COLOR_CELL
            else -> owner.color
        }
    val canPass: Boolean
        get() = obj.passable && this != NONE

    val canStop: Boolean
        get() = unit == NO_UNIT && canPass


    companion object {
        val NONE = Cell(NaturalStructures.EMPTY, x = -10, y = -10)
    }
}
