package ru.rpuxa.strategy.field.interfaces

import ru.rpuxa.strategy.players.Player

/**
 * Статические объекты на поле которые могут быть
 * построены игроком @see[Town]
 */
interface PlayerBuildings : StaticObject {
    /**
     * Все объекты построенные игроком, можно пройти
     */
    override val passable: Boolean
        get() = true

    /**
     * Игрок, владеющий данной постройкой
     */
    var owner: Player
}