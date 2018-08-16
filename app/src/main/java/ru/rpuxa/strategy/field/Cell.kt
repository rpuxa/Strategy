package ru.rpuxa.strategy.field

import ru.rpuxa.strategy.NO_PLAYER_COLOR_CELL
import ru.rpuxa.strategy.NULL_COLOR_CELL
import ru.rpuxa.strategy.field.interfaces.FieldObject
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.players.Player


class Cell(var obj: FieldObject,
           var unit: Unit = Unit.NONE,
           var owner: Player = Player.NONE,
           override var x: Int,
           override var y: Int
) : Location {

    val color: Int
        get() = when {
            this === NONE -> NULL_COLOR_CELL
            owner === Player.NONE -> NO_PLAYER_COLOR_CELL
            else -> owner.color
        }
    val canPass: Boolean
        get() = obj.passable && this != NONE

    val canStop: Boolean
        get() = unit == Unit.NONE && canPass


    companion object {
        val NONE = Cell(NaturalStructures.EMPTY, x = -10, y = -10)
    }



}

fun main(args: Array<String>) {
    "Hello".say()
}
fun String.say() = print("say: $this")
