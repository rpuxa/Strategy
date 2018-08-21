package ru.rpuxa.strategy.core.interfaces.visual

import ru.rpuxa.strategy.core.interfaces.field.Field

interface ChangeFieldAnimation : Animation {
    val field: Field
}