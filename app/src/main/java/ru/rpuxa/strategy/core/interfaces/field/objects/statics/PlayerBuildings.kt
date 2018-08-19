package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.game.Player

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