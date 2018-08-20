package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.visual.Animation

/**
 * Анимация передвижения камеры
 */
class MoveCameraAnimation(val location: Location, override val duration: Int) : Animation {
    override val async = false
}