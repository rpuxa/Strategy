package ru.rpuxa.strategy.core.implement.field.units

import ru.rpuxa.strategy.core.implement.field.info.units.SwordsmanInfo
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.units.FightingUnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.others.copyLocation
import kotlin.reflect.KClass

/**
 * Мечник. Боевой юнит эффективный против пикинёров
 */
class Swordsman(location: Location, override val owner: Player) : FightingUnit {
    override val info
            get() = SwordsmanInfo

    override var x = location.x
    override var y = location.y
    override var health = baseHealth
    override var movePoints = baseMovePoints

    override fun copy(): Swordsman = Swordsman(this.copyLocation(), owner)


}