package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.visual.view.TextureBank

class Swordsman(override var x: Int, override var y: Int) : Unit {
    override val maxMovePoints = 2
    override val icon = TextureBank.SWORD
    override val name = "Мечник"
    override val description = "Эффективен против пикинеров"

    override var health = 100
    override var movePoints = maxMovePoints

}