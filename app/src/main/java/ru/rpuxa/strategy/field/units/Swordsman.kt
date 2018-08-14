package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.visual.TextureBank

class Swordsman(override val x: Int, override val y: Int) : Unit {
    override val maxMovePoints = 2
    override val icon = TextureBank.SWORD

    override var health = 100
    override var movePoints = maxMovePoints

}