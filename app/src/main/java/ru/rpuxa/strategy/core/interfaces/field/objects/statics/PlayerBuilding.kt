package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.info.statics.PlayerBuildingInfo
import ru.rpuxa.strategy.core.interfaces.field.info.statics.StaticObjectInfo
import ru.rpuxa.strategy.core.interfaces.game.Player

/**
 * Статические объекты на поле которые могут быть
 * построены игроком @see[Town]
 */
interface PlayerBuilding : StaticObject {
    override val info: PlayerBuildingInfo

    /**
     * Игрок, владеющий данной постройкой
     */
    var owner: Player
}