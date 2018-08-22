package ru.rpuxa.strategy.core.implement.field.info.statics.player

import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.interfaces.field.info.statics.PlayerBuildingInfo

object TownInfo : PlayerBuildingInfo {

    override val passable = true
    override val icon = TexturesId.TOWN
    const val seized_icon = TexturesId.SEIZED_TOWN
    override val name = "Город"
    override val description = "Здесь вы можете создавать юниты, строить здания"

    /**
     * Максимальное количество очков работы, которое может вмещать город
     */
    const val maxWorkPoints = 500

    /**
     * Радиус области, которую выделяет город вокруг себя
     */
    const val selectionTerritory = 2

    const val capitalIcon = TexturesId.CAPITAL
}