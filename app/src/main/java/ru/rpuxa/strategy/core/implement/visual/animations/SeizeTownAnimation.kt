package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.visual.ChangeFieldAnimation

class SeizeTownAnimation(override val field: Field) : ChangeFieldAnimation {
    override val duration = -1
    override val async = false
}