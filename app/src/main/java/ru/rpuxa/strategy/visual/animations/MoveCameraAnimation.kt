package ru.rpuxa.strategy.visual.animations

import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.visual.Animation

/**
 * Анимация передвижения камеры
 */
class MoveCameraAnimation(val location: Location, override val duration: Int) : Animation