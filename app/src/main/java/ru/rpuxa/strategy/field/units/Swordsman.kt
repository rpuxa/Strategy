package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.Unit

class Swordsman(override val x: Int, override val y: Int) : Unit {
    override val maxMovePoints = 2

    override var health = 100
    override var movePoints = maxMovePoints
}