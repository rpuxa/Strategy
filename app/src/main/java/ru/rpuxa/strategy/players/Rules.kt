package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.objects.player.Town

typealias RE = ru.rpuxa.strategy.players.RuleException

object Rules {


    fun moveBeforeYouTurn() = RE("You cant move before you turn")

    fun invalidMove() = RE("Invalid move!")

    fun enoughMoney(buildable: Buildable, town: Town) =
            RE("Not enough money to build ${buildable.name}, cost ${buildable.cost}, wp ${town.workPoints}")

    fun secondTimeBuilding() =
            RE("You cant buy more then two buildings in town!")

    fun noColonistLayTown() =
            RE("Only colonist can lay the town!")

    fun layTownNoEmptyCell() =
            RE("You cant lay town on not empty cell!")

    fun enoughMovePoints() =
            RE("Needed more move point to do this")
}

class RuleException(msg: String) : Exception(msg)
