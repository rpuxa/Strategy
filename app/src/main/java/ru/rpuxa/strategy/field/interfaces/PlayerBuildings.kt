package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.players.Player

interface PlayerBuildings : FieldObject {
    override val passable: Boolean
        get() = true
    var owner: Player
}