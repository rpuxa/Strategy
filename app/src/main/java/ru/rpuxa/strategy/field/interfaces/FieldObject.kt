package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Location


interface FieldObject : Location {

    val passable: Boolean
}