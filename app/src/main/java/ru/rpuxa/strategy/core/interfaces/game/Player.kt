package ru.rpuxa.strategy.core.interfaces.game

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.interfaces.field.Field

/**
 * Интерфейс игрока
 *
 * Все кто участвует в игре (боты, люди) должны реализовывать этот интерфейс
 */
interface Player : OnServerCommandsListener {

    /**
     * Сервер, кому игрок отправляет свои действия (команды)
     * Например: переместить этого юнита на эту клетку
     */
    val executor: Server

    /**
     * Игровое поле
     */
    val field: Field

    /**
     * Цвет игрока
     */
    val color: Int

    /**
     * Количество городов у игрока
     */
    val townsCount: Int
        get() = this@Player.field.count { it.staticObject is Town && it.owner === this }

    /**
     * Дальше идут методы через которые сервер взаимодействует
     * с игроком, реагируя на его команды
     */

}

