package ru.rpuxa.strategy.core.interfaces.field.info.units

import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import kotlin.reflect.KClass

interface FightingUnitInfo : UnitInfo {

    val baseDamage: Int

    val effectiveAgainst: Array<KClass<out FightingUnit>>
}