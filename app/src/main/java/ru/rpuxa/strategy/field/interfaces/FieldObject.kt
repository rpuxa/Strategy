package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Drawable
import ru.rpuxa.strategy.field.Location


interface FieldObject : Location, Drawable {
    val passable: Boolean
}