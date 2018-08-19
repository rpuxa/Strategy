package ru.rpuxa.strategy.core.interfaces.game

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.game.players.Human
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.Buildable
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit


/**
 * Интерфейс сервера игры.
 *
 * Сервер управляет игрой, ему игроки посылают команды для
 * взаимодействия с игровым миром. Сервер следит за правилами,
 * не допуская, чтобы игроки их нарушали.
 */
interface Server {

    /**
     * Игроки
     */
    var players: Array<Player>

    /**
     * Показывает чей сейчас ход - players[turn]
     */
    var turn: Int

    /**
     * true - если игра началась
     */
    var started: Boolean

    /**
     * Человек, который управляет игрой. Не может быть
     * больше 1 в одной игре
     *
     * null - Если человека вообще нет в игре, или он
     * наблюдает за игрой
     */
    val controllingHuman: Human?

    /**
     * Метод для начала игры
     */
    fun start()

    /**
     * Далее идут команды. Они вызываются игроками
     *
     * sender - игрок который ее вызывает
     */

    /**
     * Команда для передвижения юнита
     */
    fun moveUnit(unit: Unit, toLocation: Location, sender: Player)

    /**
     * Команда для завершения текущего хода
     */
    fun endMove(sender: Player)

    /**
     * Команда для постройки [Buildable] в городе [Town]
     */
    fun build(buildable: Buildable, town: Town, sender: Player)

    /**
     * Заложить город на локации [location]
     */
    fun layTown(location: Location, sender: Player)



    /**
     * Правила, которые сервер отсылает игроку в случае их нарушения
     */
    object Rules {
        val moveBeforeYouTurn = RuleException("You cant move before you turn")

        val invalidMove = RuleException("Invalid move!")

        fun enoughMoney(buildable: Buildable, town: Town) =
                RuleException("Not enough money to build ${buildable.name}, cost ${buildable.cost}, wp ${town.workPoints}")

        val secondTimeBuilding =
                RuleException("You cant buy more then two buildings in town!")

        val noColonistLayTown =
                RuleException("Only colonist can lay the town!")

        val layTownNoEmptyCell =
                RuleException("You cant lay town on not empty cell!")

        val enoughMovePoints =
                RuleException("Needed more move point to do this")

    }

    class RuleException(msg: String) : Exception(msg)
}