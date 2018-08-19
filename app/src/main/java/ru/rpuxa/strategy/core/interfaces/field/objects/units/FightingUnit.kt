package ru.rpuxa.strategy.core.interfaces.field.objects.units

import kotlin.math.min
import kotlin.reflect.KClass

/**
 * Боевая единица, которая может атаковать другие юниты
 * и захватывать города
 */
interface FightingUnit : Unit {
    /**
     * Базовый урон
     */
    val baseDamage: Int
    /**
     * Юниты, против которых эффективен данный юнит,
     * т.е. получать бонус к урону в бою
     */
    val effectiveAgainst: Array<KClass<out FightingUnit>>

    fun damageAgainst(enemy: Unit): Int {
        var damage = baseDamage
        damage *= 1 + (health - enemy.health) / 100
        if (enemy::class in effectiveAgainst)
            damage *= 2

        return damage
    }

    override fun fight(enemy: Unit): Int {
        if (enemy is PeacefulUnit)
            return health
        enemy as FightingUnit
        val damage = damageAgainst(enemy)
        val inflictedDamage = min(damage, enemy.health)
        val percent = inflictedDamage / damage

        return health - percent * enemy.damageAgainst(this)
    }
}