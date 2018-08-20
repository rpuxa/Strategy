package ru.rpuxa.strategy.core.implement.field.info.units

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.info.units.PeacefulUnitInfo

object ColonistInfo : PeacefulUnitInfo {

    override val cost = 450
    override val name = "Поселенец"
    override val description = "Мирный житель, закладывающий новые города"
    override val icon = TexturesId.FLAG
    override val baseMovePoints = 2
}