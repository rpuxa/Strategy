package ru.rpuxa.strategy.core.implement.visual.region

import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionList
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint
import ru.rpuxa.strategy.core.others.COLOR_NONE

class StandardRegionPaint(override val list: RegionList) : RegionPaint {

    override var colorFill = COLOR_NONE
    override var colorBorder = COLOR_NONE
    override var lineEffects: Array<BoardEffect>? = null
    override var strokeWidth = 3f
    override var wide = false
}