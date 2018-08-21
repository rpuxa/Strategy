package ru.rpuxa.strategy.core.implement.field

import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.*

class CreateCell(override var staticObject: StaticObject = STATIC_OBJECT_NONE,
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
        get() = staticObject.passable && this != CELL_NONE


    override val canStop: Boolean
        get() = unit == UNIT_NONE && canPass

    override fun copy(): Cell = CreateCell(
            if (staticObject != STATIC_OBJECT_NONE) staticObject.copy() else STATIC_OBJECT_NONE,
            if (unit != UNIT_NONE) unit.copy() else UNIT_NONE,
            owner,
            x,
            y
    )
}