package ru.rpuxa.strategy.core.implement.field.units

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import kotlin.reflect.KClass

/**
 * Мечник. Боевой юнит эффективный против пикинёров
 */
class Swordsman(override var x: Int, override var y: Int) : FightingUnit {
    override val cost = 290
    override val maxMovePoints = 2
    override val icon = TexturesId.SWORD
    override val name = "Мечник"
    override val description = "Эффективен против пикинеров"
    override val damage = 25
    override val effectiveAgainst: Array<KClass<out FightingUnit>> =
            arrayOf()

    override var health = 100
    override var movePoints = maxMovePoints

}