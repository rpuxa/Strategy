package ru.rpuxa.strategy.core.interfaces.game

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.BuildableInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import kotlin.reflect.KClass


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
     * Команда для постройки [BuildableObject] в городе [Town], на локации [location]
     */
    fun build(buildableInfo: BuildableInfo, clazz: KClass<out BuildableObject>, location: Location, town: Town, sender: Player)

    /**
     * Заложить город на локации [location]
     */
    fun layTown(location: Location, sender: Player)

    fun attack(defender: Unit, attacker: Unit, sender: Player)



    /**
     * Правила, которые сервер отсылает игроку в случае их нарушения
     */
    object Rules {
        val moveBeforeYouTurn = RuleException("You cant move before you turn")

        val invalidMove = RuleException("Invalid move!")

        fun enoughMoney(buildable: BuildableInfo, town: Town) =
                RuleException("Not enough money to build ${buildable.name}, cost ${buildable.cost}, wp ${town.workPoints}")

        val secondTimeBuilding =
                RuleException("You cant buy more then two buildings in town!")

        val noColonistLayTown =
                RuleException("Only colonist can lay the town!")

        val layTownNoEmptyCell =
                RuleException("You cant lay town on not empty cell!")

        val enoughMovePoints =
                RuleException("Needed more move point to do this")

        val actionWithNotFromYouObject =
                RuleException("You can not interact with other player's objects")

    }

    class RuleException(msg: String) : Exception(msg)
}