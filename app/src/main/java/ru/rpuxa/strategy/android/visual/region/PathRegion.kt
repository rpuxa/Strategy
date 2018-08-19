package ru.rpuxa.strategy.android.visual.region

import android.graphics.Path
import ru.rpuxa.strategy.core.interfaces.visual.region.Region

class PathRegion(val path: Path = Path()) : Region {

    override fun moveTo(x: Float, y: Float) = path.moveTo(x, y)

    override fun lineTo(x: Float, y: Float) = path.lineTo(x, y)

    override fun close() = path.close()

}