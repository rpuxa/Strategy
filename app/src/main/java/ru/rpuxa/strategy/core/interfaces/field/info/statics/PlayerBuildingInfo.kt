package ru.rpuxa.strategy.core.interfaces.field.info.statics

interface PlayerBuildingInfo : StaticObjectInfo {
    override val passable: Boolean
        get() = true
}