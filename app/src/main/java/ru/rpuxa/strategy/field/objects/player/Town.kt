package ru.rpuxa.strategy.field.objects.player

import ru.rpuxa.strategy.field.interfaces.PlayerBuildings

class Town(override var x: Int, override var y: Int) : PlayerBuildings {
    val selectionTerretory = 3
}