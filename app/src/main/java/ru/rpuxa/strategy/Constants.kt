package ru.rpuxa.strategy

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import kotlin.math.sqrt


const val COLOR_NONE = Color.TRANSPARENT
const val NO_PLAYER_COLOR_CELL = Color.LTGRAY
const val NULL_COLOR_CELL = Color.BLACK
const val BACKGROUND_FIELD_COLOR = Color.BLACK
const val TERRITORY_COLOR_BOARD = Color.YELLOW
const val UNIT_REGION_MOVE_BORDER_COLOR = Color.GREEN

const val CELL_RADIUS = 128f
@JvmField val CELL_INSIDE_RADIUS = sqrt(3f) / 2 * CELL_RADIUS
const val BOARD_TERRITORY_WIDTH = 15f
const val BOARD_TERRITORY_RADIUS_CORNERS = 30f
const val UNIT_REGION_MOVE_BORDER_WIDTH = 7f
const val UNIT_REGION_MOVE_RADIUS_CORNERS = 20f



const val UNIT_ICON_RADIUS = 40f
const val UNIT_ICON_X = 0f
@JvmField val UNIT_ICON_Y = -(CELL_RADIUS - 2 * UNIT_ICON_RADIUS / sqrt(3f)) + 5

const val UNIT_TEXTURE_X = UNIT_ICON_X
const val UNIT_TEXTURE_HEIGHT = 2 * CELL_RADIUS - 2 * UNIT_ICON_RADIUS - 30
@JvmField val UNIT_TEXTURE_Y = UNIT_ICON_Y + UNIT_ICON_RADIUS + 5 + UNIT_TEXTURE_HEIGHT / 2

@JvmField val BOARD_TERRITORY_EFFECTS = composeEffectsVararg(
        DashPathEffect(floatArrayOf(50f, 50f), 0f),
        CornerPathEffect(BOARD_TERRITORY_RADIUS_CORNERS)
)
@JvmField val UNIT_REGION_MOVE_BORDER_EFFECTS =
        CornerPathEffect(UNIT_REGION_MOVE_RADIUS_CORNERS)

const val INITIAL_PERFORMANCE = 35
const val TOWN_COUNT_PERFORMANCE_FACTOR = 1.05f
const val TOWN_LEVEL_PERFORMANCE_FACTOR = 1.15f

const val OPEN_OBJ_INFO_DIRECTION = 350L