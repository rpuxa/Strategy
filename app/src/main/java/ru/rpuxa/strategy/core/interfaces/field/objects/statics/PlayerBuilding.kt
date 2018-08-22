package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.info.statics.PlayerBuildingInfo
import ru.rpuxa.strategy.core.interfaces.game.Player

/**
 * Статические объекты на поле которые могут быть
 * построены игроком @see[Town]
 */
abstract class PlayerBuilding : StaticObject() {
    abstract override val info: PlayerBuildingInfo

    /**
     * Игрок, владеющий данной постройкой
     */
    abstract var owner: Player
}