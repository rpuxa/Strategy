package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.FightingUnit
import ru.rpuxa.strategy.visual.view.TextureBank
import kotlin.reflect.KClass

class Swordsman(override var x: Int, override var y: Int) : FightingUnit {
    override val maxMovePoints = 2
    override val icon = TextureBank.SWORD
    override val name = "Мечник"
    override val description = "Эффективен против пикинеров"
    override val damage = 25
    override val effectiveAgainist: Array<KClass<out FightingUnit>> =
            arrayOf()

    override var health = 100
    override var movePoints = maxMovePoints

}