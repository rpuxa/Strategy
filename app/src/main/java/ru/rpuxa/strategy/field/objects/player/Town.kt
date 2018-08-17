package ru.rpuxa.strategy.field.objects.player

import ru.rpuxa.strategy.field.interfaces.PlayerBuildings
import ru.rpuxa.strategy.players.Player
import ru.rpuxa.strategy.visual.view.TextureBank

class Town(override var x: Int, override var y: Int, override var owner: Player) : PlayerBuildings {
    val selectionTerritory = 3
    override val icon = TextureBank.TOWN
    override val name = "Город"
    override val description = "Здесь вы можете создавать юниты, строить здания"
    val maxWorkPoints = 500

    var workPoints = 250
    var bought = false
}


