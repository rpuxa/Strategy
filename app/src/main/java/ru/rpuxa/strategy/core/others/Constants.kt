package ru.rpuxa.strategy.core.others

import android.graphics.Color
import ru.rpuxa.strategy.core.implement.field.CreateCell
import ru.rpuxa.strategy.core.implement.visual.boardEffects.CornerBoardEffect
import ru.rpuxa.strategy.core.implement.visual.boardEffects.DashBoardEffect
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.Buildable
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.interfaces.game.Server
import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect
import kotlin.math.sqrt


const val COLOR_NONE = 0
const val NO_PLAYER_COLOR_CELL = Color.LTGRAY
const val NULL_COLOR_CELL = Color.BLACK
const val BACKGROUND_FIELD_COLOR = Color.BLACK
const val TERRITORY_COLOR_BOARD = Color.YELLOW
const val UNIT_REGION_MOVE_BORDER_COLOR = Color.GREEN

const val CELL_RADIUS = 128f
@JvmField
val CELL_INSIDE_RADIUS = sqrt(3f) / 2 * CELL_RADIUS
const val BOARD_TERRITORY_WIDTH = 15f
const val BOARD_TERRITORY_RADIUS_CORNERS = 30f
const val UNIT_REGION_MOVE_BORDER_WIDTH = 7f
const val UNIT_REGION_MOVE_RADIUS_CORNERS = 20f


const val UNIT_ICON_RADIUS = 40f
const val UNIT_ICON_X = 0f
@JvmField
val UNIT_ICON_Y = -(CELL_RADIUS - 2 * UNIT_ICON_RADIUS / sqrt(3f)) + 5

const val UNIT_TEXTURE_X = UNIT_ICON_X
const val UNIT_TEXTURE_HEIGHT = 2 * CELL_RADIUS - 2 * UNIT_ICON_RADIUS - 30
@JvmField
val UNIT_TEXTURE_Y = UNIT_ICON_Y + UNIT_ICON_RADIUS + 5 + UNIT_TEXTURE_HEIGHT / 2

@JvmField
val BOARD_TERRITORY_EFFECTS = arrayOf(
        DashBoardEffect(floatArrayOf(50f, 50f), 0f),
        CornerBoardEffect(BOARD_TERRITORY_RADIUS_CORNERS)
)
@JvmField
val UNIT_REGION_MOVE_BORDER_EFFECTS = arrayOf<BoardEffect> (
        CornerBoardEffect(UNIT_REGION_MOVE_RADIUS_CORNERS)
)

const val INITIAL_PERFORMANCE = 35
const val TOWN_COUNT_PERFORMANCE_FACTOR = 1.05f
const val TOWN_LEVEL_PERFORMANCE_FACTOR = 1.15f

const val OPEN_OBJ_INFO_DIRECTION = 350L


/**
 * Метод для обозначения нереализованных полей
 */
fun fail(): Nothing =
        throw UnsupportedOperationException("NONE constant in doesn't implement methods")

/**
 * Константа, обозначающая отсутствие игрока
 * Методы не реализованы
 */
val PLAYER_NONE = object : Player {

    override val executor: Server
        get() = fail()
    override val field: Field
        get() = fail()
    override val color: Int
        get() = fail()

    override fun onRuleViolate(rule: Server.RuleException) = fail()

    override fun onMoveUnit(from: Location, to: Location, sender: Player) = fail()

    override fun onStart() = fail()

    override fun onMoveStart() = fail()

    override fun onBuild(buildable: Buildable) = fail()

    override fun onTownLaid(location: Location) = fail()
}

/**
 * Красный игрок (для дебага)
 */
val PLAYER_RED: Player = object : Player by PLAYER_NONE {
    override val color = Color.RED
}


/**
 * Константа которая показывает отсутствие статического объекта на клетке
 *
 * Все методы не реализованы, кроме [passable], т.к через
 * пустую клетку можно пройти юнитом
 */
val STATIC_OBJECT_NONE = object : StaticObject {
    override val icon: Int
        get() = fail()
    override val passable = true
    override val description: String
        get() = fail()
    override val name: String
        get() = fail()
    override var x: Int
        get() = fail()
        set(_) = fail()
    override var y: Int
        get() = fail()
        set(_) = fail()
}


/**
 * Константа которая показывает отсутствие юнита на клетки
 *
 * Все методы не реализованы
 */
val UNIT_NONE = object : Unit {
    override val cost: Int
        get() = fail()
    override val description: String
        get() = fail()
    override val name: String
        get() = fail()
    override var movePoints: Int
        get() = fail()
        set(_) = fail()
    override val maxMovePoints: Int
        get() = fail()
    override var x: Int
        get() = fail()
        set(_) = fail()
    override var y: Int
        get() = fail()
        set(_) = fail()
    override var health: Int
        get() = fail()
        set(_) = fail()
    override val icon: Int
        get() = fail()
}

/**
 * Константа, обозначающая отсутствие клетки
 *
 * Применяется, за границами поля [Field]
 */
val CELL_NONE = CreateCell(STATIC_OBJECT_NONE, x = -10, y = -10)