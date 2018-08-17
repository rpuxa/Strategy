package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.objects.player.Town

object Rules {

    fun moveBeforeYouTurn() = RuleException("You cant move before you turn")

    fun invalidMove() = RuleException("Invalid move!")

    fun enoughMoney(buildable: Buildable, town: Town) =
            RuleException("Not enough money to build ${buildable.name}, cost ${buildable.cost}, wp ${town.workPoints}")

    fun secondTimeBuilding() =
            RuleException("You cant buy more then two buildings in town!")
}

class RuleException(msg: String) : Exception(msg)
