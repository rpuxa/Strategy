package ru.rpuxa.strategy.core.implement.field.info.units

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.info.units.FightingUnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import kotlin.reflect.KClass

object SwordsmanInfo : FightingUnitInfo {
    override val cost = 290
    override val baseDamage = 25
    override val baseMovePoints = 2
    override val icon = TexturesId.SWORD
    override val name = "Мечник"
    override val description = "Эффективен против пикинеров"
    override val effectiveAgainst: Array<KClass<out FightingUnit>> =
            arrayOf()
}