package ru.rpuxa.strategy.android.visual.view

import android.graphics.Canvas
import android.graphics.Color
import ru.rpuxa.strategy.core.interfaces.field.Location

class HealthDrawerText(override val view: FieldSurfaceView, value: Int, location: Location) : FieldSurfaceView.Inner {
    val text = (if (value > 0) "+" else "") + value
    var worldX: Float
    var worldY: Float
    var deltaY = 0f
    private val baseColor = if (value > 0) Color.GREEN else Color.RED

    init {
        val (x, y) = view.locationToWorld(location)
        worldX = x + 10
        worldY = y - 10
    }

    fun paint(canvas: Canvas) {
        view.paint.textSize = 40f
        view.paint.color = baseColor
        canvas.drawText(text, worldX, worldY + deltaY, view.paint)
    }
}
