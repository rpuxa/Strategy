package ru.rpuxa.strategy.core.implement.visual.animations

import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.visual.Animation
import ru.rpuxa.strategy.core.interfaces.visual.ChangeFieldAnimation

class UpdateAnimation(override val field: Field) : ChangeFieldAnimation {
    override val async = false
    override val duration = -1
}