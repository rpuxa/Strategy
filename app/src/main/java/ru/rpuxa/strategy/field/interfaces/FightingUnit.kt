package ru.rpuxa.strategy.field.interfaces

import kotlin.reflect.KClass

/**
 * Боевая единица, которая может атаковать другие юниты
 * и захватывать города
 */
interface FightingUnit : Unit {
    /**
     * Базовый урон
     */
    val damage: Int
    /**
     * Юниты, против которых эффективен данный юнит,
     * т.е. получать бонус к урону в бою
     */
    val effectiveAgainst: Array<KClass<out FightingUnit>>
}