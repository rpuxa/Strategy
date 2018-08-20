package ru.rpuxa.strategy.core.interfaces.game

import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit

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
    fun onRuleViolate(rule: Server.RuleException)

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
     * Вызывается при постройке [BuildableObject] в городе
     */
    fun onBuild(buildableObject: BuildableObject)

    /**
     * Вызывается когда поселенец закладывает город
     */
    fun onTownLaid(location: Location)

    fun onAttack(moveFromLocation: Location, attackFromLocation: Location, attacker: Unit, defender: Unit, defenderHit: Int, attackerHit: Int)

}

