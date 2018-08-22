package ru.rpuxa.strategy.core.implement.field.units

import ru.rpuxa.strategy.core.implement.field.info.units.SwordsmanInfo
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.copyLocation

/**
 * Мечник. Боевой юнит эффективный против пикинёров
 */
class Swordsman(location: Location, override val owner: Player) : FightingUnit() {

    constructor(location: Location, owner: Player, id: Long, health: Int, movePoints: Int) : this(location, owner) {
        super.id = id
        this.health = health
        this.movePoints = movePoints
    }

    override val info
            get() = SwordsmanInfo

    override var x = location.x
    override var y = location.y
    override var health = baseHealth
    override var movePoints = baseMovePoints

    override fun copy(): Swordsman = Swordsman(this.copyLocation(), owner, id, health, movePoints)
}