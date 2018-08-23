package ru.rpuxa.strategy.android.visual.view

import ru.rpuxa.strategy.core.geometry.Point
import ru.rpuxa.strategy.core.others.pt

internal class Camera(override val view: FieldSurfaceView) : FieldSurfaceView.Inner {
    var x = 0f
    var y = 0f
    var width = 900f
    var height = -1f
        get() {
            if (field == -1f)
                throw IllegalStateException("Height is not initialized!")
            return field
        }

    fun projectToWorld(x: Float, y: Float): Point {
        return x * width / view.width + this.x - width / 2 pt y * height / view.height + this.y - height / 2
    }
}