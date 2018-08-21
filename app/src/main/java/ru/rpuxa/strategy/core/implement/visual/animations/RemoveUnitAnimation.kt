package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.Animation

class RemoveUnitAnimation(val unit: Unit) : Animation {
    override val async = false
    override val duration = -1
}