package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.visual.Animation

class HealthAnimation(val location: Location, val health: Int, override val duration: Int) : Animation {
    override val async = true
}