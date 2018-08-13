package ru.rpuxa.strategy.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import ru.rpuxa.strategy.CELL_INSIDE_RADIUS
import ru.rpuxa.strategy.CELL_RADIUS
import ru.rpuxa.strategy.STROKE_WIDTH
import ru.rpuxa.strategy.field.GameBoardCreator
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.objects.Cell
import ru.rpuxa.strategy.players.Human
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()
    val camera = Camera()

    fun setControlHuman(human: Human) {
        setOnTouchListener { _, event ->
            human.onTouch(this, event!!)
            true
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!
        canvas.drawGameField(GameBoardCreator.square(6,6))
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
        paint.style = Paint.Style.STROKE
        drawPath(path, paint)
    }

    private fun Path.hexagon(x: Float, y: Float, radius: Float) {
        val (fromX, fromY) = camera.projectToCanvas(x, y + radius)
        moveTo(fromX, fromY)
        for (i in 0..6) {
            val angle = i * PI.toFloat() / 3
            val (x1, y1) = camera.projectToCanvas(x + radius * sin(angle), y + radius * cos(angle))
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

    fun getCellCoords(x: Int, y: Int) = getCellCoords(x.toFloat(), y.toFloat())

    fun getCellCoords(x:Float, y: Float): Pair<Float, Float> {
        return x * CELL_INSIDE_RADIUS to
                y * 3 * CELL_RADIUS +
                if (x % 2 == 0f)
                    0f
                else
                    CELL_RADIUS * 1.5f
    }
    private fun Canvas.drawGameField(field: HexagonField) {
        for ((x, y, cell) in field.iterator) {
            if (cell == Cell.NONE)
                continue
            val (cellX, cellY) = getCellCoords(x.toFloat(), y)
            drawCell(cellX, cellY,Color.BLACK, cell.color)
        }
    }

    inner class Camera {
        var x = 0f
        var y = 0f
        var width = 2000f
        var height = 250f

        fun projectToCanvas(x: Float, y: Float): Pair<Float, Float> {
            return (x - (this.x - width / 2)) / width * this@FieldView.width to (y - (this.y - height / 2)) / height * this@FieldView.height
        }

        fun projectToWorld(x: Float, y: Float): Pair<Float, Float> {
            return x * width / this@FieldView.width + this.x + width / 2 to y * height / this@FieldView.height + this.y + height / 2
        }
    }


}