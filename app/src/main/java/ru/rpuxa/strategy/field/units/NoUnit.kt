package ru.rpuxa.strategy.field.units

import ru.rpuxa.strategy.field.Unit

interface NoUnit : Unit

val NO_UNIT = object : NoUnit {
    override var health = -1
}