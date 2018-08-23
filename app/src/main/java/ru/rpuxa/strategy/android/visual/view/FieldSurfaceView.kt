package ru.rpuxa.strategy.android.visual.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import ru.rpuxa.strategy.android.others.composeEffects
import ru.rpuxa.strategy.android.visual.FieldView
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

class FieldSurfaceView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), SurfaceHolder.Callback, FieldVisualizer {

    override fun surfaceDestroyed(holder: SurfaceHolder?) {}

    override val animator = Animator()
    override var field: Field? = null


    private val paint = Paint()
    internal val camera = Camera()
    internal var rebuildTerritories = true
    private val textures = TextureBank(resources)
    private val icons = IconsBank(textures)
    private lateinit var regionBuilder: RegionBuilder
    private lateinit var territories: Array<RegionPaint>
    private val selections = ArrayList<RegionPaint>()
    internal val fieldObjectsLocation = FieldObjectsLocation()
    internal lateinit var human: Human
    private val activity = context as Activity
    private val headerController = HeaderInfoController()
    private val healthList = ArrayList<FieldView.HealthDrawerText>()

    override fun surfaceCreated(holder: SurfaceHolder?) {
        setOnTouchListener(Controller())

        activity.main_obj_info.obj_close.setOnClickListener {
            closeInfoHeader(true)
        }
        activity.main_obj_info.visibility = View.GONE
        activity.next_move.setOnClickListener {
            closeInfoHeader(true)
            human.executor.endMove(human)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        canvas!!
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
        invalidate()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        camera.height = camera.width / width * height
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

    @Synchronized
    override fun invalidate() {
        TODO("переименовать в update и доделать вью, перенести все классы в папку view  Сильно логается FieldBiew при  большой карте поэтому мы и переделаывем в surface" +
                "до скорого а я пошел делать adbWireless")
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

    internal fun RegionPaint.paintFill(canvas: Canvas, paint: Paint) {
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

    internal fun RegionPaint.paintBorder(canvas: Canvas, paint: Paint) {
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

    internal fun Array<BoardEffect>.convertToPathEffects() = composeEffects(
            Array(size) {
                val boardEffect = this[it]
                when (boardEffect) {
                    is CornerBoardEffect -> CornerPathEffect(boardEffect.radius)
                    is DashBoardEffect -> DashPathEffect(boardEffect.intervals, boardEffect.phase)
                    else -> throw UnsupportedOperationException("$boardEffect not supported")
                }
            }
    )

    internal fun Canvas.drawBitmap(bitmapFromBank: Int, centerX: Float, centerY: Float, height: Float) {
        val bitmap = textures.getProportionalScaled(bitmapFromBank, height.toInt())
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - height / 2, paint)
    }

    internal fun Canvas.drawBitmap(bitmap: Bitmap, centerX: Float, centerY: Float) {
        drawBitmap(bitmap, centerX - bitmap.width / 2, centerY - bitmap.height / 2, paint)
    }

    internal interface Inner {
        val view: FieldSurfaceView
    }
}