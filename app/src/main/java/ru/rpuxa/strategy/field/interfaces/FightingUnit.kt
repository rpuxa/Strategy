package ru.rpuxa.strategy.field.interfaces

import kotlin.reflect.KClass

interface FightingUnit : Unit {
    val damage: Int
    val effectiveAgainst: Array<KClass<out FightingUnit>>
}