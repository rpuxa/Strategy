package ru.rpuxa.strategy.field.objects.player

import ru.rpuxa.strategy.field.interfaces.PlayerBuildings
import ru.rpuxa.strategy.visual.view.TextureBank

class Town(override var x: Int, override var y: Int) : PlayerBuildings {
    val selectionTerritory = 3
    override val icon = TextureBank.UNIT
}