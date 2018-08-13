package ru.rpuxa.strategy.field.objects

import ru.rpuxa.strategy.NO_PLAYER_COLOR_CELL
import ru.rpuxa.strategy.NULL_COLOR_CELL
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.interfaces.FieldObject
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.field.units.NO_UNIT
import ru.rpuxa.strategy.players.Player


class Cell(var obj: FieldObject, val unit: Unit = NO_UNIT, var owner: Player = Player.NONE) {
    val color: Int
        get() = when {
            this === NONE -> NULL_COLOR_CELL
            obj === NaturalStructures.EMPTY -> NO_PLAYER_COLOR_CELL
            else -> owner.color
        }


    companion object {
        val NONE = Cell(NaturalStructures.EMPTY)
    }
}
