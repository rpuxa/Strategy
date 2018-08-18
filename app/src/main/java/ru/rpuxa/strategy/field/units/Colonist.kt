package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.interfaces.PeacefulUnit
import ru.rpuxa.strategy.visual.view.TextureBank

/**
 * Поселенец. Закладывает новые города
 */
class Colonist(override var x: Int, override var y: Int) : PeacefulUnit {
    override val cost = 450
    override val name = "Поселенец"
    override val description = "Мирный житель, закладывающий новые города"
    override val icon = TextureBank.FLAG
    override val maxMovePoints = 2

    override var movePoints = maxMovePoints
    override var health = 100
}