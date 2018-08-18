package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.objects.player.Town

private typealias RE = ru.rpuxa.strategy.players.RuleException


/**
 * Правила, которые сервер отсылает игроку в случае их нарушения
 */
object Rules {


    val moveBeforeYouTurn = RE("You cant move before you turn")

    val invalidMove = RE("Invalid move!")

    fun enoughMoney(buildable: Buildable, town: Town) =
            RE("Not enough money to build ${buildable.name}, cost ${buildable.cost}, wp ${town.workPoints}")

    val secondTimeBuilding =
            RE("You cant buy more then two buildings in town!")

    val noColonistLayTown =
            RE("Only colonist can lay the town!")

    val layTownNoEmptyCell =
            RE("You cant lay town on not empty cell!")

    val enoughMovePoints =
            RE("Needed more move point to do this")
}

class RuleException(msg: String) : Exception(msg)
