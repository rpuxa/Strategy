package ru.rpuxa.strategy.visual.view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.FightingUnit
import ru.rpuxa.strategy.field.HexagonField
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.field.units.Swordsman
import ru.rpuxa.strategy.geometry.Point
import ru.rpuxa.strategy.geometry.pt
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.players.Player
import ru.rpuxa.strategy.visual.Animation
import ru.rpuxa.strategy.visual.FieldAnimator
import ru.rpuxa.strategy.visual.FieldVisualizer
import ru.rpuxa.strategy.visual.animations.MoveCameraAnimation
import ru.rpuxa.strategy.visual.animations.MoveUnitAnimation
import java.util.*
import kotlin.collections.ArrayList


class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs), FieldVisualizer {

    override val animator = Animator()

    override val cameraWidth: Float
        get() = camera.width

    override val cameraHeight: Float
        get() = camera.height

    override val width: Float
        get() = getWidth().toFloat()

    override val height: Float
        get() = getHeight().toFloat()

    private val paint = Paint()
    private val camera = Camera()
    private var rebuild = true
    private val textures = TextureBank(resources)

    private lateinit var regionBuilder: RegionBuilder

    private lateinit var territories: Array<RegionPaint>
    private val selections = ArrayList<RegionPaint>()

    private val unitsShift = UnitsShiftMap()

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

        for (cell in regionBuilder.field) {
            val (worldX, worldY) = locationToWorld(cell)
            val hasBuild = cell.obj != NaturalStructures.EMPTY
            if (cell.unit != Unit.NONE) {
                val (shiftX, shiftY) = unitsShift[cell.unit]
                canvas.drawBitmap(cell.unit.icon, worldX + shiftX + UNIT_ICON_X, worldY + shiftY + UNIT_ICON_Y, 2 * UNIT_ICON_RADIUS, 2 * UNIT_ICON_RADIUS)
                if (!hasBuild || shiftX != 0f || shiftY != 0f) {
                    val index = if (cell.unit is FightingUnit) TextureBank.UNIT else TextureBank.PEACEFUL_UNIT
                    canvas.drawBitmap(index, worldX + shiftX + UNIT_TEXTURE_X, worldY + shiftY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
                }
            }
            if (hasBuild)
                canvas.drawBitmap(cell.obj.icon, worldX + UNIT_TEXTURE_X, worldY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
        }
    }

    private fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, width: Float, height: Float) {
        val bitmap = textures.getScaled(bitmapFromBank, width.toInt(), height.toInt())
        drawBitmap(bitmap, centerX - width / 2, centerY - height / 2, paint)
    }

    private fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, height: Float) {
        val bitmap = textures.getProportionalScaled(bitmapFromBank, height.toInt())
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - height / 2, paint)
    }

    override fun draw(field: Field) {
        if (field !is HexagonField)
            throw IllegalStateException("FieldView cannot view not HexagonFields")
        field[0, 0].obj = Town(0, 0, Player.RED)
        field[0, 0].unit = Swordsman(0, 0)
        regionBuilder = RegionBuilder(this, field)
        rebuild = true
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        camera.height = camera.width / w * h
    }

    override fun locationToWorld(x: Int, y: Int) = x * CELL_INSIDE_RADIUS pt
            y * 3 * CELL_RADIUS +
            if (x % 2 == 0)
                0f
            else
                CELL_RADIUS * 1.5f

    override fun select(region: RegionPaint) {
        selections.add(region)
    }

    override fun unselect(region: RegionPaint) {
        selections.remove(region)
    }

    override fun setControllingHuman(human: Human) {
        setOnTouchListener { _, event ->
            human.onTouch(event!!)
            true
        }
    }

    override fun projectToWorld(x: Float, y: Float) = camera.projectToWorld(x, y)

    override fun translateCamera(deltaX: Float, deltaY: Float) {
        camera.x += deltaX
        camera.y += deltaY
        invalidate()
    }

    override fun invalidate() {
        (context as Activity).runOnUiThread {
            super.invalidate()
        }
    }

    inner class Animator internal constructor() : FieldAnimator {

        override fun animate(animation: Animation) {
            synchronized(animations) {
                animations.addLast(animation)
            }
            startAsyncAnimate()
        }


        private val animations = ArrayDeque<Animation>()

        @Volatile
        private var asyncStarted = false

        private fun startAsyncAnimate() {
            if (asyncStarted)
                return
            asyncStarted = true

            Thread {
                while (true) {
                    var empty = false
                    var animation: Animation? = null
                    synchronized(animations) {
                        if (animations.isEmpty()) {
                            empty = true
                            return@synchronized
                        }
                        animation = animations.pollFirst()
                    }

                    if (empty) {
                        asyncStarted = false
                        return@Thread
                    }

                    asyncAnimation(animation!!)
                }
            }.start()
        }

        private fun asyncAnimation(animation: Animation) = when (animation) {
            is MoveUnitAnimation -> moveUnit(animation)
            is MoveCameraAnimation -> moveCameraToLocation(animation)
            else -> throw UnsupportedOperationException("Animation ${animation.javaClass.name} not supported")
        }

        private fun moveUnit(animation: MoveUnitAnimation) {
            val (worldLocationX, worldLocationY) = locationToWorld(animation.to)
            val (worldUnitX, worldUnitY) = locationToWorld(animation.from)
            val deltaX = worldUnitX - worldLocationX
            val deltaY = worldUnitY - worldLocationY
            valueAnimate(animation) {
                val reverse = 1f - it
                unitsShift[animation.unit] = deltaX * reverse pt deltaY * reverse
                invalidate()
            }
            unitsShift.clear()
            invalidate()
        }

        private fun moveCameraToLocation(animation: MoveCameraAnimation) {
            val (worldLocationX, worldLocationY) = locationToWorld(animation.location)
            val deltaX = worldLocationX - camera.x
            val deltaY = worldLocationY - camera.y
            val cameraX = camera.x
            val cameraY = camera.y
            valueAnimate(animation) {
                camera.x = cameraX + deltaX * it
                camera.y = cameraY + deltaY * it
            }
        }

        private inline fun valueAnimate(animation: Animation, block: (Float) -> kotlin.Unit) = valueAnimate(animation.duration, block)

        private inline fun valueAnimate(duration: Int, block: (Float) -> kotlin.Unit) {
            val start = System.currentTimeMillis()
            val end = start + duration
            while (true) {
                val currentTime = System.currentTimeMillis()
                if (currentTime >= end)
                    break
                val timeGone = currentTime - start
                block(timeGone.toFloat() / duration)
            }
        }


    }

    private inner class Camera {
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
            return x * width / this@FieldView.width + this.x - width / 2 pt y * height / this@FieldView.height + this.y - height / 2
        }

//        fun projectToCanvas(x: Float, y: Float) =
//                (x - (this.x + width / 2)) * this@FieldView.width / width to (y - (this.y + height / 2)) * this@FieldView.height / height
    }

    private inner class UnitsShiftMap : HashMap<Unit, Point>() {

        override operator fun get(key: Unit): Point {
            val shift = super.get(key)
            if (shift == null) {
                val newPoint = 0f pt 0f
                this[key] = newPoint
                return newPoint
            }
            return shift
        }

    }
}