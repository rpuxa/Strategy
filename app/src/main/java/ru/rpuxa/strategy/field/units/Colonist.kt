package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.PeacefulUnit
import ru.rpuxa.strategy.visual.view.TextureBank

class Colonist(override var x: Int, override var y: Int) : PeacefulUnit {
    override val name = "Поселенец"
    override val description = "Мирный житель, закладывающий новые города"
    override val icon = TextureBank.SWORD
    override val maxMovePoints = 2

    override var movePoints = maxMovePoints
    override var health = 100
}