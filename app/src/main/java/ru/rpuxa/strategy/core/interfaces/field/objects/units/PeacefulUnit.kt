package ru.rpuxa.strategy.core.interfaces.field.objects.units

/**
 * Мирный юнит, может быть убит без боя
 * боевым юнитом [FightingUnit]
 */
abstract class PeacefulUnit : Unit() {



    override fun fight(enemy: Unit) = 0
}