package ru.rpuxa.strategy.core.implement.field

import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.*

class CreateCell(override var obj: StaticObject = STATIC_OBJECT_NONE,
                 override var unit: Unit = UNIT_NONE,
                 override var owner: Player = PLAYER_NONE,
                 override var x: Int,
                 override var y: Int
) : Cell {

    override val color: Int
        get() = when {
            this === CELL_NONE -> NULL_COLOR_CELL
            owner === PLAYER_NONE -> NO_PLAYER_COLOR_CELL
            else -> owner.color
        }

    override val canPass: Boolean
        get() = obj.passable && this != CELL_NONE


    override val canStop: Boolean
        get() = unit == UNIT_NONE && canPass
}