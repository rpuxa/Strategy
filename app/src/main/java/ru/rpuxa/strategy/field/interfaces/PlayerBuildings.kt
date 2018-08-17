package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.players.Player

interface PlayerBuildings : StaticObject {
    override val passable: Boolean
        get() = true
    var owner: Player
}