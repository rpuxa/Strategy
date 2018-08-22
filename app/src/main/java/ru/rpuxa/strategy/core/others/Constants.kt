package ru.rpuxa.strategy.core.others

import android.graphics.Color
import ru.rpuxa.strategy.core.implement.field.CreateCell
import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.visual.boardEffects.CornerBoardEffect
import ru.rpuxa.strategy.core.implement.visual.boardEffects.DashBoardEffect
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.info.statics.StaticObjectInfo
import ru.rpuxa.strategy.core.interfaces.field.info.units.UnitInfo
import ru.rpuxa.strategy.core.interfaces.field.objects.BuildableObject
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.interfaces.game.Player
import ru.rpuxa.strategy.core.interfaces.game.Server
import ru.rpuxa.strategy.core.interfaces.visual.BoardEffect
import java.util.*
import kotlin.math.sqrt


const val COLOR_NONE = 0
const val NO_PLAYER_COLOR_CELL = Color.LTGRAY
const val NULL_COLOR_CELL = Color.BLACK
const val BACKGROUND_FIELD_COLOR = Color.BLACK
const val TERRITORY_COLOR_BOARD = Color.YELLOW
const val UNIT_REGION_MOVE_BORDER_COLOR = Color.BLUE
const val UNIT_REGION_ATTACK_BORDER_COLOR = Color.RED

const val CELL_RADIUS = 128f
@JvmField
val CELL_INSIDE_RADIUS = sqrt(3f) / 2 * CELL_RADIUS
const val BOARD_TERRITORY_WIDTH = 15f
const val BOARD_TERRITORY_RADIUS_CORNERS = 40f
const val UNIT_REGION_MOVE_BORDER_WIDTH = 7f
const val UNIT_REGION_MOVE_RADIUS_CORNERS = 20f
const val UNIT_ICON_RADIUS = 40f
const val UNIT_ICON_X = 0f
const val DISTANCE_FROM_UNIT_ICON_TO_HEALTH_BAR = 20f
const val HEALTH_BAR_WIDTH = 10f
const val HEALTH_BAR_HEIGHT = UNIT_ICON_RADIUS * 2



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
val TERRITORY_EFFECTS = arrayOf<BoardEffect>(
        CornerBoardEffect(BOARD_TERRITORY_RADIUS_CORNERS)
)


@JvmField
val UNIT_REGION_MOVE_BORDER_EFFECTS = arrayOf<BoardEffect>(
        CornerBoardEffect(UNIT_REGION_MOVE_RADIUS_CORNERS)
)

const val INITIAL_PERFORMANCE = 35
const val TOWN_COUNT_PERFORMANCE_FACTOR = 1.05f
const val TOWN_LEVEL_PERFORMANCE_FACTOR = 1.15f

const val OPEN_OBJ_INFO_DIRECTION = 350L

val random = Random()


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

    override fun onMoveUnit(from: Location, to: Location, sender: Player, fieldAfterMove: Field) = fail()

    override fun onStart() = fail()

    override fun onMoveStart() = fail()

    override fun onBuild(buildableObject: BuildableObject, fieldAfterBuild: Field) = fail()

    override fun onTownLaid(location: Location, fieldAfterTownLaid: Field) = fail()

    override fun onAttack(moveFromLocation: Location, attackFromLocation: Location,
                          attacker: Unit, defender: Unit, defenderHit: Int,
                          attackerHit: Int, killed: Boolean, fieldAfterAttack: Field) = fail()

    override fun onSeizeTown(staticObject: Town, fieldAfterSeize: Field) = fail()

    override fun onStifleRebellion(town: Town, fieldAfterSeize: Field) = fail()

    override fun onTownDestroyed(location: Location, fieldAfterTownDestroyed: Field) = fail()
}

/**
 * Красный игрок (для дебага)
 */
val PLAYER_RED: Player = object : Player by PLAYER_NONE {
    override val color = Color.RED
    override val townsCount = 1
}


/**
 * Константа которая показывает отсутствие статического объекта на клетке
 *
 * Все методы не реализованы, кроме [passable], т.к через
 * пустую клетку можно пройти юнитом
 */
val STATIC_OBJECT_NONE = object : StaticObject() {
    override val info: StaticObjectInfo
        get() = fail()
    override val passable: Boolean
        get() = true
    override var x: Int
        get() = fail()
        set(_) = fail()
    override var y: Int
        get() = fail()
        set(_) = fail()

    override fun copy() = fail()
}


/**
 * Константа которая показывает отсутствие юнита на клетки
 *
 * Все методы не реализованы
 */
val UNIT_NONE = object : Unit() {
    override val info: UnitInfo
        get() = fail()
    override var movePoints: Int
        get() = fail()
        set(_) = fail()

    override var x: Int
        get() = fail()
        set(_) = fail()
    override var y: Int
        get() = fail()
        set(_) = fail()
    override var health: Int
        get() = fail()
        set(_) = fail()
    override val owner: Player
        get() = fail()

    override fun fight(enemy: Unit) = fail()

    override fun copy() = fail()
}

/**
 * Константа, обозначающая отсутствие клетки
 *
 * Применяется, за границами поля [Field]
 */
val CELL_NONE = CreateCell(STATIC_OBJECT_NONE, x = -10, y = -10)

infix fun Location.equals(other: Location) = x == other.x && y == other.y