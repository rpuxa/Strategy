package ru.rpuxa.strategy.core.interfaces.field.info.units

import ru.rpuxa.strategy.core.interfaces.field.info.BuildableInfo

interface UnitInfo : BuildableInfo {

    val baseMovePoints: Int

    val baseHealth: Int
        get() = 100
}