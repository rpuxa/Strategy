package ru.rpuxa.strategy.visual.animations

import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.visual.Animation

class MoveUnitAnimation(val from: Location, val to: Location, val unit: Unit, override val duration: Int) : Animation