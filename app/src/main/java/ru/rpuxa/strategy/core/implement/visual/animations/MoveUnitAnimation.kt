package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.Animation

/**
 * Анимация передвижения юнита
 */
class MoveUnitAnimation(val from: Location, val to: Location, val unit: Unit, val updateAfterAnimation: Boolean, override val duration: Int) : Animation {
    override val async = false
}