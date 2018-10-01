package ru.rpuxa.strategy.android.visual.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import ru.rpuxa.strategy.android.others.composeEffects
import ru.rpuxa.strategy.android.visual.IconsBank
import ru.rpuxa.strategy.android.visual.TextureBank
import ru.rpuxa.strategy.android.visual.region.PathRegion
import ru.rpuxa.strategy.android.visual.region.PathRegionBuilder
import ru.rpuxa.strategy.core.implement.field.HexagonField
import ru.rpuxa.strategy.core.implement.game.players.Human
import ru.rpuxa.strategy.core.implement.visual.TexturesId
import ru.rpuxa.strategy.core.implement.visual.boardEffects.CornerBoardEffect
import ru.rpuxa.strategy.core.implement.visual.boardEffects.DashBoardEffect
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect
import ru.rpuxa.strategy.core.interfaces.visual.FieldVisualizer
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionBuilder
import ru.rpuxa.strategy.core.interfaces.visual.region.RegionPaint
import ru.rpuxa.strategy.core.others.*

/**
 * Реализация [FieldSurfaceView] для андроид
 */
class FieldSurfaceView(context: Context, attrs: AttributeSet) :
        SurfaceView(context, attrs),
        SurfaceHolder.Callback,
        FieldVisualizer {

    init {
        holder.addCallback(this)
    }

    override val animator = Animator(this)

    override var field: Field? = null
    val paint = Paint()


    val camera = Camera(this)
    var rebuildTerritories = true
    val textures = TextureBank(resources)
    val icons = IconsBank(textures)
    lateinit var regionBuilder: RegionBuilder
    lateinit var territories: Array<RegionPaint>
    val selections = ArrayList<RegionPaint>()
    val fieldObjectsLocation = FieldObjectsLocation(this)
    lateinit var human: Human
    val activity get() = context as Activity
    val headerController = HeaderInfoController(this)
    val healthList = ArrayList<HealthDrawerText>()

    private val drawerThreadLock = Object()
    private lateinit var drawingThread: Thread

    override fun surfaceCreated(holder: SurfaceHolder) {
        setOnTouchListener(Controller(this))

        activity.main_obj_info.obj_close.setOnClickListener {
            closeInfoHeader(true)
        }
        activity.main_obj_info.visibility = View.GONE
        activity.next_move.setOnClickListener {
            closeInfoHeader(true)
            human.executor.endMove(human)
        }
        drawingThread =
                Thread {
                    while (!drawingThread.isInterrupted) {
                        synchronized(drawerThreadLock) {
                            val canvas = holder.lockCanvas(null)
                            synchronized(holder) {
                                drawCanvas(canvas)
                            }
                            holder.unlockCanvasAndPost(canvas)
                            drawerThreadLock.wait()
                        }
                    }
                }
        drawingThread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        camera.height = camera.width / width * height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        drawingThread.interrupt()
    }

    override fun onCreate() {
    }

    private fun drawCanvas(canvas: Canvas) {
        val scale = camera.width / canvas.width
        canvas.translate(canvas.width / 2f - camera.x / scale, canvas.height / 2f - camera.y / scale)
        canvas.scale(1 / scale, 1 / scale)

        paint.color = BACKGROUND_FIELD_COLOR
        paint.pathEffect = null
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0f

        canvas.drawPaint(paint)

        if (rebuildTerritories) {
            regionBuilder.field = field!!
            territories = regionBuilder.createFromTerritories()
        }

        territories.forEach { it.paintFill(canvas, paint) }
        selections.forEach { it.paintFill(canvas, paint) }
        territories.forEach { it.paintBorder(canvas, paint) }
        selections.forEach { it.paintBorder(canvas, paint) }

        rebuildTerritories = false

        for ((obj, _) in fieldObjectsLocation) {
            if (obj.isNone)
                continue

            if (obj is Unit) {
                val (unitX, unitY) = fieldObjectsLocation[obj]!!.toPoint()
                //draw icon unit
                canvas.drawBitmap(icons[obj.icon, obj.owner.color, UNIT_ICON_RADIUS.toInt()], unitX + UNIT_ICON_X, unitY + UNIT_ICON_Y)
                //draw heath bar
                val leftHealthBar = unitX + UNIT_ICON_X + UNIT_ICON_RADIUS + DISTANCE_FROM_UNIT_ICON_TO_HEALTH_BAR
                val topHealthBar = unitY + UNIT_ICON_Y - HEALTH_BAR_HEIGHT / 2 + HEALTH_BAR_HEIGHT * (1 - obj.health.toFloat() / obj.baseHealth)
                val rightHealthBar = leftHealthBar + HEALTH_BAR_WIDTH
                val bottomHealthBar = unitY + UNIT_ICON_Y + HEALTH_BAR_HEIGHT / 2
                paint.color = getHeathBarColor(obj)
                paint.pathEffect = null
                paint.style = Paint.Style.FILL
                paint.strokeWidth = 0f
                canvas.drawRect(leftHealthBar, topHealthBar, rightHealthBar, bottomHealthBar, paint)
                //draw unit
                if (field!![obj].staticObject == STATIC_OBJECT_NONE || locationToWorld(obj) != unitX pt unitY) {
                    val index = if (obj is FightingUnit) TexturesId.UNIT else TexturesId.PEACEFUL_UNIT
                    canvas.drawBitmap(index, unitX + UNIT_TEXTURE_X, unitY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
                }
            }

            if (obj is StaticObject) {
                //draw static objects
                val (objectX, objectY) = locationToWorld(obj)
                canvas.drawBitmap(obj.icon, objectX + UNIT_TEXTURE_X, objectY + UNIT_TEXTURE_Y, UNIT_TEXTURE_HEIGHT)
            }
        }

        healthList.toTypedArray().forEach { it.paint(canvas) }
    }

    override fun draw(field: Field) {
        if (field !is HexagonField)
            throw IllegalStateException("FieldView cannot view not HexagonFields")
        val nullField = this.field == null
        this.field = field.copy()
        if (nullField)
            fieldObjectsLocation.updateLocations(this.field as HexagonField)
        regionBuilder = PathRegionBuilder(this, this.field as HexagonField)
        rebuildTerritories = true
        update()
    }

    override fun locationToWorld(x: Int, y: Int) =
            x * CELL_INSIDE_RADIUS pt
                    y * 3 * CELL_RADIUS + if (x % 2 == 0) 0f else CELL_RADIUS * 1.5f

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
        update()
    }

    override fun zoomCamera(value: Float) {
        camera.width += value
        camera.height += value / width * height
        update()
    }

    override fun update() {
        synchronized(drawerThreadLock) {
            drawerThreadLock.notify()
        }
    }

    @Deprecated("", ReplaceWith("super.invalidate()", "android.view.SurfaceView"))
    override fun invalidate() {
        super.invalidate()
    }

    override fun openInfoHeader(selectedObject: FieldObject) {
        headerController.setHeaderInfo(selectedObject)
    }

    override fun closeInfoHeader(invalidate: Boolean) {
        headerController.closeInfoHeader(invalidate)
    }

    fun RegionPaint.paintFill(canvas: Canvas, paint: Paint) {
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

    fun RegionPaint.paintBorder(canvas: Canvas, paint: Paint) {
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

    fun Array<BoardEffect>.convertToPathEffects() = composeEffects(
            Array(size) {
                val boardEffect = this[it]
                when (boardEffect) {
                    is CornerBoardEffect -> CornerPathEffect(boardEffect.radius)
                    is DashBoardEffect -> DashPathEffect(boardEffect.intervals, boardEffect.phase)
                    else -> throw UnsupportedOperationException("$boardEffect not supported")
                }
            }
    )

    fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, height: Float) {
        val bitmap = textures.getProportionalScaled(bitmapFromBank, height.toInt())
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - height / 2, paint)
    }

    fun Canvas.drawBitmap(bitmap: Bitmap, centerX: Float, centerY: Float) {
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - bitmap.height / 2, paint)
    }

    interface Inner {
        val view: FieldSurfaceView
    }
}