package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.Unit

interface CommandExecutor {
    var players: Array<Player>
    var turn: Int
    var started: Boolean

    fun start()

    fun moveUnit(unit: Unit, location: Location, sender: Player)
}