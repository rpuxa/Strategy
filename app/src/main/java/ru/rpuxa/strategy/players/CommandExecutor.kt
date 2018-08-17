package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.field.objects.player.Town

interface CommandExecutor {
    var players: Array<Player>
    var turn: Int
    var started: Boolean
    val controllingHuman: Human?

    fun start()

    fun moveUnit(unit: Unit, location: Location, sender: Player)

    fun endMove(sender: Player)

    fun build(buildable: Buildable, town: Town, sender: Player)
}