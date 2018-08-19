package ru.rpuxa.strategy.core.implement.field.units

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.objects.units.PeacefulUnit
import ru.rpuxa.strategy.core.interfaces.game.Player

/**
 * Поселенец. Закладывает новые города
 */
class Colonist(override var x: Int, override var y: Int, override val owner: Player) : PeacefulUnit {
    override val cost = 450
    override val name = "Поселенец"
    override val description = "Мирный житель, закладывающий новые города"
    override val icon = TexturesId.FLAG
    override val maxMovePoints = 2

    override var movePoints = maxMovePoints
    override var health = 100
}