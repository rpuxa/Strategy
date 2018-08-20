package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.visual.Animation
import ru.rpuxa.strategy.core.others.fail

class EndAnimations : Animation {
    override val async: Boolean
        get() = false
    override val duration: Int
        get() = fail()
}