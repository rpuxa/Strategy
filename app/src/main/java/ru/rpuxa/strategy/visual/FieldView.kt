package ru.rpuxa.strategy.visual

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.rpuxa.strategy.BACKGROUND_FIELD_COLOR
import ru.rpuxa.strategy.CELL_INSIDE_RADIUS
import ru.rpuxa.strategy.CELL_RADIUS
import ru.rpuxa.strategy.composeEffects
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.units.Swordsman
import ru.rpuxa.strategy.players.Human


class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs), VisualHexagonField {


    private val paint = Paint()
    val camera = Camera()
    var rebuild = true
    var field: HexagonField? = null
    lateinit var regionBuilder: RegionBuilder
    val selections = ArrayList<Selection>()


    lateinit var regions: Array<Region>

    fun setControlHuman(human: Human) {
        setOnTouchListener { _, event ->
            human.onTouch(this, event!!)
            true
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        if (field == null)
            return
        canvas!!

        val scale = camera.width / canvas.width
        canvas.translate(canvas.width / 2f - camera.x / scale, canvas.height / 2f - camera.y / scale)
        canvas.scale(scale, scale)

        paint.color = BACKGROUND_FIELD_COLOR
        canvas.drawPaint(paint)

        if (rebuild) {
            regions = regionBuilder.extractTerritories()
        }

        for (region in regions) {
            paint.style = Paint.Style.FILL
            paint.color = region.color
            canvas.drawPath(region.border, paint)
        }

        selections.forEach {
            paint.style = Paint.Style.STROKE
            paint.color = it.color
            paint.pathEffect = composeEffects(it.style)
            canvas.drawPath(it.border, paint)
        }

        rebuild = false
    }

    override fun draw(field: HexagonField) {
        this.field = field
        regionBuilder = RegionBuilder(this, field)
        rebuild = true
        selections.add(regionBuilder.SelectionBuilder(Color.YELLOW, CornerPathEffect(15f)).moveSelection(Swordsman(3, 3)))
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        camera.height = camera.width / w * h
    }

    fun getCellCoords(x: Int, y: Int) = getCellCoords(x.toFloat(), y.toFloat())

    fun getCellCoords(x: Float, y: Float): Pair<Float, Float> {
        return x * CELL_INSIDE_RADIUS to
                y * 3 * CELL_RADIUS +
                if (x % 2 == 0f)
                    0f
                else
                    CELL_RADIUS * 1.5f
    }

    inner class Camera {
        var x = 0f
        var y = 0f
        var width = 2000f
        var height = 250f

        fun projectToWorld(x: Float, y: Float): Pair<Float, Float> {
            return x * width / this@FieldView.width + this.x + width / 2 to y * height / this@FieldView.height + this.y + height / 2
        }
    }


}