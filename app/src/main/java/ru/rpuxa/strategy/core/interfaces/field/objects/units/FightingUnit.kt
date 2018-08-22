package ru.rpuxa.strategy.core.interfaces.field.objects.units

import ru.rpuxa.strategy.core.interfaces.field.info.units.FightingUnitInfo
import kotlin.math.min
import kotlin.reflect.KClass

/**
 * Боевая единица, которая может атаковать другие юниты
 * и захватывать города
 */
abstract class FightingUnit : Unit() {
    abstract override val info: FightingUnitInfo

    /**
     * Базовый урон
     */
    val baseDamage: Int
        get() = info.baseDamage
    /**
     * Юниты, против которых эффективен данный юнит,
     * т.е. получать бонус к урону в бою
     */
    val effectiveAgainst: Array<KClass<out FightingUnit>>
        get() = info.effectiveAgainst

    fun damageAgainst(enemy: Unit): Int {
        var damage = baseDamage.toFloat()
        damage *= 1 + (health * baseHealth - enemy.health * enemy.baseHealth).toFloat() / (baseHealth * enemy.baseHealth)
        if (enemy::class in effectiveAgainst)
            damage *= 2

        if (damage < 5)
            damage = 5f
        return damage.toInt()
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