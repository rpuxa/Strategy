package ru.rpuxa.strategy.android.visual

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import kotlinx.android.synthetic.main.town_info.view.*
import kotlinx.android.synthetic.main.unit_info.view.*
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.android.others.composeEffects
import ru.rpuxa.strategy.android.visual.region.PathRegion
import ru.rpuxa.strategy.core.geometry.Point
import ru.rpuxa.strategy.core.implement.field.HexagonField
import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.field.units.Colonist
import ru.rpuxa.strategy.core.implement.game.players.Human
import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.implement.visual.animations.MoveCameraAnimation
import ru.rpuxa.strategy.core.implement.visual.animations.MoveUnitAnimation
import ru.rpuxa.strategy.core.implement.visual.boardEffects.CornerBoardEffect
import ru.rpuxa.strategy.core.implement.visual.boardEffects.DashBoardEffect
import ru.rpuxa.strategy.core.implement.visual.region.StandardRegionBuilder
import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.Animation
import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect
import ru.rpuxa.strategy.core.interfaces.visual.FieldAnimator
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionBuilder
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint
import ru.rpuxa.strategy.core.others.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Реализация [FieldVisualizer] для android устройств
 */
class FieldView(context: Context, attrs: AttributeSet) : View(context, attrs), FieldVisualizer {

    override val animator = Animator()
    override lateinit var field: Field


    private val paint = Paint()
    private val camera = Camera()
    private var rebuild = true
    private val textures = TextureBank(resources)
    private lateinit var regionBuilder: RegionBuilder
    private lateinit var territories: Array<RegionPaint>
    private val selections = ArrayList<RegionPaint>()
    private val unitsShift = UnitsShiftMap()
    private lateinit var human: Human
    private val activity = context as Activity
    private val headerController = HeaderInfoController()

    init {
        setOnTouchListener(Controller())
    }

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
            val hasBuild = cell.obj != STATIC_OBJECT_NONE
            if (cell.unit != UNIT_NONE) {
                val (shiftX, shiftY) = unitsShift[cell.unit]
                canvas.drawBitmap(cell.unit.icon, worldX + shiftX + UNIT_ICON_X, worldY + shiftY + UNIT_ICON_Y, 2 * UNIT_ICON_RADIUS, 2 * UNIT_ICON_RADIUS)
                if (!hasBuild || shiftX != 0f || shiftY != 0f) {
                    val index = if (cell.unit is FightingUnit) TexturesId.UNIT else TexturesId.PEACEFUL_UNIT
                    canvas.drawBitmap(index, worldX + shiftX + UNIT_TEXTURE_X, worldY + shiftY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
                }
            }
            if (hasBuild)
                canvas.drawBitmap(cell.obj.icon, worldX + UNIT_TEXTURE_X, worldY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
        }
    }

    override fun draw(field: Field) {
        if (field !is HexagonField)
            throw IllegalStateException("FieldView cannot view not HexagonFields")
        activity.next_move.setOnClickListener {
            closeInfoHeader(true)
            human.executor.endMove(human)
        }
        regionBuilder = StandardRegionBuilder(this, field)
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

    override fun deselect(region: RegionPaint) {
        selections.remove(region)
    }

    override fun setControllingHuman(human: Human) {
        this.human = human
    }

    override fun projectToWorld(x: Float, y: Float) = camera.projectToWorld(x, y)

    override fun translateCamera(deltaX: Float, deltaY: Float) {
        camera.x += deltaX
        camera.y += deltaY
        invalidate()
    }

    override fun zoomCamera(value: Float) {
        camera.width += value
        camera.height += value / width * height
        invalidate()
    }

    override fun invalidate() {
        activity.runOnUiThread {
            super.invalidate()
        }
    }

    override fun openInfoHeader(selectedObject: FieldObject) {
        headerController.setHeaderInfo(selectedObject)
    }

    override fun closeInfoHeader(invalidate: Boolean) {
        headerController.closeInfoHeader(invalidate)
    }

    private fun RegionPaint.paintFill(canvas: Canvas, paint: Paint) {
        if (colorFill != COLOR_NONE) {
            paint.pathEffect = null
            paint.color = colorFill
            paint.style = Paint.Style.FILL

            list.forEach {
                if (it is PathRegion)
                    canvas.drawPath(it.path, paint)
                else
                    throw IllegalStateException("FieldView cant draw not PathRegion")
            }
        }
    }

    private fun RegionPaint.paintBorder(canvas: Canvas, paint: Paint) {
        if (colorBorder != COLOR_NONE) {
            if (lineEffects != null)
                paint.pathEffect = lineEffects!!.convertToPathEffects()
            paint.style = Paint.Style.STROKE
            paint.color = colorBorder
            paint.strokeWidth = strokeWidth

            list.forEach {
                if (it is PathRegion)
                    canvas.drawPath(it.path, paint)
                else
                    throw IllegalStateException("FieldView cant draw not PathRegion")
            }
        }
    }

    private fun Array<BoardEffect>.convertToPathEffects() = composeEffects(
            Array(size) {
                val boardEffect = this[it]
                when (boardEffect) {
                    is CornerBoardEffect -> CornerPathEffect(boardEffect.radius)
                    is DashBoardEffect -> DashPathEffect(boardEffect.intervals, boardEffect.phase)
                    else -> throw UnsupportedOperationException("$boardEffect not supported")
                }
            }
    )

    private fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, width: Float, height: Float) {
        val bitmap = textures.getScaled(bitmapFromBank, width.toInt(), height.toInt())
        drawBitmap(bitmap, centerX - width / 2, centerY - height / 2, paint)
    }

    private fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, height: Float) {
        val bitmap = textures.getProportionalScaled(bitmapFromBank, height.toInt())
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - height / 2, paint)
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

    private inner class Controller : OnTouchListener {

        private var lastTouch0 = null as Array<Float>?
        private var lastDist: Float? = null
        private var zoomed = false
        private var firstTouch = null as Array<Float>?

        private fun onTouch(event: MotionEvent) {
            fun Cell.click(chosenObj: Boolean) {
                if (human.moveMode.running) {
                    if (this in human.moveMode.selection) {
                        human.executor.moveUnit(human.moveMode.unit, this, human)
                        return
                    }
                    closeInfoHeader(true)
                    return
                }

                if (this == CELL_NONE || unit == UNIT_NONE && obj == STATIC_OBJECT_NONE) {
                    closeInfoHeader(true)
                    return
                }

                openInfoHeader(if (chosenObj) obj else unit)
            }

            fun click(x: Float, y: Float) {
                val (worldX, worldY) = projectToWorld(x, y)
                var chosenUnit = false
                val findCell = field.find {
                    val (cellWorldX, cellWorldY) = locationToWorld(it)
                    chosenUnit = dist(
                            worldX, worldY,
                            cellWorldX + UNIT_ICON_X,
                            cellWorldY + UNIT_ICON_Y

                    ) <= UNIT_ICON_RADIUS
                    dist(cellWorldX, cellWorldY, worldX, worldY) <= CELL_INSIDE_RADIUS
                }
                findCell?.click(!chosenUnit) ?: CELL_NONE.click(false)
            }


            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    lastTouch0 = arrayOf(event.x, event.y)
                    firstTouch = arrayOf(event.x, event.y)
                }

                MotionEvent.ACTION_MOVE -> when (event.pointerCount) {
                    1 -> if (!zoomed) {
                        translateCamera((lastTouch0!![0] - event.x) / width * camera.width, (lastTouch0!![1] - event.y) / height * camera.height)
                        lastTouch0 = arrayOf(event.x, event.y)
                    }

                    2 -> {
                        val currentDist = dist(
                                event.getAxisValue(MotionEvent.AXIS_X, 0),
                                event.getAxisValue(MotionEvent.AXIS_Y, 0),
                                event.getAxisValue(MotionEvent.AXIS_X, 1),
                                event.getAxisValue(MotionEvent.AXIS_Y, 1)
                        )
                        if (lastDist != null) {
                            zoomCamera(lastDist!! - currentDist)
                        }

                        lastDist = currentDist

                        zoomed = true
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (!zoomed && firstTouch!![0] == event.x && firstTouch!![1] == event.y) {
                        click(event.x, event.y)
                    }
                    zoomed = false
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            onTouch(event!!)
            return true
        }

    }

    private inner class HeaderInfoController {
        private var currentSelection: FieldObject? = null

        init {
            activity.main_obj_info.obj_close.setOnClickListener {
                closeInfoHeader(true)
            }
            activity.main_obj_info.visibility = View.GONE
        }

        fun setHeaderInfo(selectedObject: FieldObject) {
            fun moveModeOn() {
                val regionPaint = StandardRegionBuilder(this@FieldView, field).createFromUnitMove(selectedObject as Unit)
                        .board(UNIT_REGION_MOVE_BORDER_COLOR)
                        .effects(UNIT_REGION_MOVE_BORDER_EFFECTS)
                        .boardWidth(UNIT_REGION_MOVE_BORDER_WIDTH)
                select(regionPaint)
                human.moveMode.selection = regionPaint
                human.moveMode.on(selectedObject)
                invalidate()
            }

            val update = currentSelection == selectedObject

            if (update) {
                human.moveMode.off(true)
                moveModeOn()
            }

            if (currentSelection != null)
                human.moveMode.off(true)

            currentSelection = selectedObject

            activity.main_obj_info.obj_name.text = selectedObject.name
            activity.main_obj_info.obj_description.text = selectedObject.description
            when (selectedObject) {
                is Town -> {
                    choseMenu(TOWN)
                    val view = activity.main_obj_info.town_info_include
                    view.town_bar_work_points.progress = selectedObject.workPoints
                    view.town_work_points.text = selectedObject.workPoints.toString()
                    view.town_bar_work_points.max = selectedObject.maxWorkPoints
                    view.town_max_work_points.text = selectedObject.maxWorkPoints.toString()
                    view.town_performance.text = selectedObject.performance.toString()
                    view.town_build_list.adapter = TownBuildingsAdapter(textures) {
                        if (it.cost <= selectedObject.workPoints && !selectedObject.bought) {
                            it.x = selectedObject.x
                            it.y = selectedObject.y
                            human.executor.build(it, selectedObject, human)
                            closeInfoHeader(true)
                        }
                    }
                }

                is Unit -> {
                    choseMenu(UNIT)
                    val view = activity.main_obj_info.unit_info_include

                    view.build_town.visibility =
                            if (selectedObject is Colonist && selectedObject.movePoints > 0)
                                View.VISIBLE
                            else
                                View.GONE
                    view.unit_hp_bar.progress = selectedObject.health
                    view.unit_move.visibility = if (selectedObject.movePoints == 0) View.GONE else View.VISIBLE
                    view.unit_info_move_point.text = selectedObject.movePoints.toString()
                    view.unit_info_max_move_points.text = selectedObject.maxMovePoints.toString()

                    view.unit_move.setOnClickListener {
                        moveModeOn()
                    }

                    view.build_town.setOnClickListener {
                        human.executor.layTown(selectedObject, human)
                        closeInfoHeader(true)
                    }
                }

                else -> throw UnsupportedOperationException("Unknown type ${selectedObject.javaClass.name}")
            }

            if (!update)
                open()
        }

        private val menus = arrayOf(
                R.id.town_info_include,
                R.id.unit_info_include
        )
        private val TOWN = 0
        private val UNIT = 1

        private fun choseMenu(id: Int) {
            val menuId = menus[id]
            menus.forEach {
                activity.main_obj_info.findViewById<View>(it).visibility =
                        if (it == menuId)
                            View.VISIBLE
                        else
                            View.GONE
            }
        }

        fun closeInfoHeader(invalidate: Boolean) {
            human.moveMode.off(invalidate)
            currentSelection = null
            close()
        }

        private var opened = false
        private fun move(open: Boolean, duration: Long) {
            if (opened == open)
                return
            val view = activity.main_obj_info
            view.visibility = if (open) View.VISIBLE else View.GONE
            /* if (open) {
                 view.visibility = View.INVISIBLE
             }
              val mainHeight = activity.main_view.height
              val first = if (open) 0 else -view.height
              val second = if (open) -view.height else 0
              val animation = ValueAnimator.ofInt(first, second)
              animation.duration = duration
              animation.addUpdateListener {
                  if (open && view.is)
                  val shift = it.animatedValue as Int
                  val i = mainHeight + shift

                  if (i >= 0) {
                      val width = view.height
                      view.layout(0, i, view.width, mainHeight + view.height + shift)
                  }
              }
              animation.start()*/
            opened = open
        }

        fun open() = move(true, OPEN_OBJ_INFO_DIRECTION)

        fun close() = move(false, OPEN_OBJ_INFO_DIRECTION)
    }
}