package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.interfaces.PeacefulUnit
import ru.rpuxa.strategy.visual.view.TextureBank

class Colonist(override var x: Int, override var y: Int) : PeacefulUnit {
    override val cost = 450
    override val name = "Поселенец"
    override val description = "Мирный житель, закладывающий новые города"
    override val icon = TextureBank.SWORD
    override val maxMovePoints = 2

    override var movePoints = maxMovePoints
    override var health = 100

    companion object {
        val INSTANCE = Colonist(Int.MAX_VALUE, Int.MAX_VALUE)
    }
}