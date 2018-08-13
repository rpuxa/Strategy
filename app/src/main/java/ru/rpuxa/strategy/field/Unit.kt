package ru.rpuxa.strategy.field

interface Unit : Location {
    var health: Int
    var movePoints: Int

    val maxMovePoints: Int
}