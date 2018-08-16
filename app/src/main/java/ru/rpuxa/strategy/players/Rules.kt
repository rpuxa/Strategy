package ru.rpuxa.strategy.players

object Rules {

    fun moveBeforeYouTurn() = RuleException("You cant move before you turn")

    fun invalidMove() = RuleException("Invalid move!")
}

class RuleException(msg: String) : Exception(msg)
