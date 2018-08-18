package ru.rpuxa.strategy.players

import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.field.objects.player.Town

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
}