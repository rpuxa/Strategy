package ru.rpuxa.strategy.core.implement.field.statics.player

import ru.rpuxa.strategy.core.implement.field.info.statics.player.TownInfo
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.copyLocation

class Capital : Town {

    constructor(location: Location, owner: Player) : super(location, owner)

    constructor(location: Location, owner: Player, id: Long, workPoints: Int,
                bought: Boolean, level: Int, movesToDestroy: Int) : super(location, owner, id, workPoints, bought, level, movesToDestroy)


    override val icon: Int
        get() = TownInfo.capitalIcon

    override fun copy(): Capital = Capital(this.copyLocation(), owner, id, workPoints, bought, level, movesToDestroy)
}