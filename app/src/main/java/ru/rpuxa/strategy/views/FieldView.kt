package ru.rpuxa.strategy.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.R.attr.y
import android.R.attr.x
import android.graphics.Color
import android.graphics.Color.LTGRAY
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val CELL_RADIUS = 128f
const val STROKE_WIDTH = 20f


class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    private lateinit var camera: Camera

    override fun onDraw(canvas: Canvas?) {
        canvas!!
        canvas.drawCell(500f, 500f, Color.BLACK, Color.LTGRAY)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        camera.height = camera.width / w * h
    }

    private fun Canvas.drawCell(x: Float, y: Float, strokeColor: Int, cellColor: Int) {
        paint.color = cellColor
        drawHexagon(x, y, CELL_RADIUS)
        val path = Path()
        path.hexagon(x, y, CELL_RADIUS)
        paint.strokeWidth = STROKE_WIDTH
        paint.color = strokeColor
        drawPath(path, paint)
    }

    private fun Path.hexagon(x: Float, y: Float, radius: Float) {
        val (fromX, fromY) = camera.project(x, y + radius)
        moveTo(fromX, fromY)
        for (i in 0..6) {
            val angle = i * PI.toFloat() / 3
            val (x1, y1) = camera.project(x + radius * sin(angle), y + radius * cos(angle))
            lineTo(x1, y1)
        }

        close()
    }

    private fun Canvas.drawHexagon(x: Float, y: Float, radius: Float) {
        val path = Path()
        path.hexagon(x, y, radius)
        paint.style = Paint.Style.FILL
        drawPath(path, paint)
    }

    inner class Camera {
        var x = 500f
        var y = 500f
        var width = 1000f
        var height = 1000f

        fun project(x: Float, y: Float): Pair<Float, Float> {
            return x - (this.x - width / 2) to y - (this.y - height / 2)
        }
    }
}