package ru.rpuxa.strategy.core.interfaces.field.objects.statics

import ru.rpuxa.strategy.core.interfaces.field.Owned
import ru.rpuxa.strategy.core.interfaces.field.info.statics.PlayerBuildingInfo

/**
 * Статические объекты на поле которые могут быть
 * построены игроком @see[Town]
 */
abstract class PlayerBuilding : StaticObject(), Owned {
    abstract override val info: PlayerBuildingInfo
}