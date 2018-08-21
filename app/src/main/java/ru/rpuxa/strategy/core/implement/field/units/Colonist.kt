package ru.rpuxa.strategy.core.implement.field.units

import ru.rpuxa.strategy.core.implement.field.info.units.ColonistInfo
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.units.UnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.units.PeacefulUnit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.copyLocation

/**
 * Поселенец. Закладывает новые города
 */
class Colonist(location: Location, override val owner: Player) : PeacefulUnit {
    override val info: UnitInfo
        get() = ColonistInfo
    override var x = location.x
    override var y = location.y
    override var movePoints = baseMovePoints
    override var health = baseHealth

    override fun copy(): Colonist = Colonist(this.copyLocation(), owner)

}