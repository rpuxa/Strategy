package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.field.Location

interface FieldObject : Location {
    val icon: Int
    val name: String
    val description: String
}