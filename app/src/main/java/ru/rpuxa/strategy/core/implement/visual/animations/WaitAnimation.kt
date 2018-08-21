package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.visual.Animation

class WaitAnimation(override val duration: Int) : Animation {
    override val async = false
}