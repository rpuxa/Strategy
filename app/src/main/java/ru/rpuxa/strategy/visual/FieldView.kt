package ru.rpuxa.strategy.visual

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.rpuxa.strategy.BACKGROUND_FIELD_COLOR
import ru.rpuxa.strategy.CELL_INSIDE_RADIUS
import ru.rpuxa.strategy.CELL_RADIUS
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.interfaces.FieldObject
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.players.Player


class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs), VisualHexagonField {


    private val paint = Paint()
    val camera = Camera()
    private var rebuild = true
    private val textures = TextureBank(resources)

    private lateinit var regionBuilder: RegionBuilder


    private lateinit var territories: Array<RegionPaint>
    val selections = ArrayList<RegionPaint>()

    fun setControlHuman(human: Human) {
        setOnTouchListener { _, event ->
            human.onTouch(this, event!!)
            true
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        canvas!!
        textures.updateSize(camera.width.toInt(), camera.height.toInt())
        val scale = camera.width / canvas.width
        canvas.translate(canvas.width / 2f - camera.x / scale, canvas.height / 2f - camera.y / scale)
        canvas.scale(1 / scale, 1 / scale)

        paint.color = BACKGROUND_FIELD_COLOR
        paint.pathEffect = null
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f

        canvas.drawPaint(paint)

        if (rebuild) {
            territories = regionBuilder.createFromTerritories()
        }

        territories.forEach { it.paintFill(canvas, paint) }
        selections.forEach { it.paintFill(canvas, paint) }
        territories.forEach { it.paintBorder(canvas, paint) }
        selections.forEach { it.paintBorder(canvas, paint) }

        rebuild = false

        for ((x, y, cell) in regionBuilder.field) {
            val (worldX, worldY) = getCellCoords(x, y)
            val hasBuild = cell.obj != NaturalStructures.EMPTY
            if (cell.unit != Unit.NONE && hasBuild)
                //TODO ОТОбражение иконок юнитов и объектов
        }
    }


    override fun draw(field: HexagonField) {
        field[0, 0].owner = Player.RED
        field[1, 0].owner = Player.RED
        field[2, 0].owner = Player.RED
        field[3, 0].owner = Player.RED
        regionBuilder = RegionBuilder(this, field)
        rebuild = true
       // selections.add(regionBuilder.SelectionBuilder(Color.YELLOW, CornerPathEffect(15f)).moveSelection(Swordsman(3, 3)))
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
        var width = 900f
        var height = 250f

        fun projectToWorld(x: Float, y: Float): Pair<Float, Float> {
            return x * width / this@FieldView.width + this.x + width / 2 to y * height / this@FieldView.height + this.y + height / 2
        }
    }


}