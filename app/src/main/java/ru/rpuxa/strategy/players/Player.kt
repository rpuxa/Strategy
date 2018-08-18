package ru.rpuxa.strategy.players

import android.graphics.Color
import ru.rpuxa.strategy.field.Fallible
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.Field
import ru.rpuxa.strategy.field.objects.player.Town

/**
 * Интерфейс игрока
 *
 * Все кто участвует в игре (боты, люди) должны реализовывать этот интерфейс
 */
interface Player {

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
        get() = this@Player.field.count { it.obj is Town && it.owner === this }

    /**
     * Дальше идут методы через которые сервер взаимодействует
     * с игроком, реагируя на его команды
     */

    /**
     * Вызывается, когда игрок нарушает правила (по мнению сервера),
     * в [rule] содержится информация о нарушении
     */
    fun onRuleViolate(rule: RuleException)

    /**
     * Вызывается, когда чей то юнит передвигается с локации [from] на [to]
     * sender - игрок, передвинувший юнит
     */
    fun onMoveUnit(from: Location, to: Location, sender: Player)

    /**
     * Вызывается при начале игры
     */
    fun onStart()

    /**
     * Вызывается при начале хода
     */
    fun onMoveStart()

    /**
     * Вызывается при постройке [Buildable] в городе
     */
    fun onBuild(buildable: Buildable)

    /**
     * Вызывается когда поселенец закладывает город
     */
    fun onTownLaid(location: Location)


    companion object : Fallible {

        /**
         * Константа, обозначающая отсутствие игрока
         * Методы не реализованы
         */
        val NONE = object : Player {

            override val executor: Server
                get() = fail()
            override val field: Field
                get() = fail()
            override val color: Int
                get() = fail()

            override fun onRuleViolate(rule: RuleException) = fail()

            override fun onMoveUnit(from: Location, to: Location, sender: Player) = fail()

            override fun onStart() = fail()

            override fun onMoveStart() = fail()

            override fun onBuild(buildable: Buildable) = fail()

            override fun onTownLaid(location: Location) = fail()
        }

        /**
         * Красный игрок (для дебага)
         */
        val RED: Player = object : Player by NONE {
            override val color = Color.RED
        }
    }
}

