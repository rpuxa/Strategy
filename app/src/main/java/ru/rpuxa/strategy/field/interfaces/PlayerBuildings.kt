package ru.rpuxa.strategy.field.interfaces

interface PlayerBuildings : FieldObject {
    override val passable: Boolean
        get() = true
}