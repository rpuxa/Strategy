package ru.rpuxa.strategy.field

import kotlin.reflect.KClass

interface FightingUnit : Unit {
    val damage: Int
    val effectiveAgainist: Array<KClass<out FightingUnit>>
}