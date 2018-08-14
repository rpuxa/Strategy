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


const val CELL_RADIUS = 128f
@JvmField
val CELL_INSIDE_RADIUS = sqrt(3f) / 2 * CELL_RADIUS
const val BOARD_TERRITORY_WIDTH = 15f

const val BOARD_TERRITORY_RADIUS_CORNERS = 30f

@JvmField
val BOARD_TERRITORY_EFFECTS = composeEffectsVararg(
        DashPathEffect(floatArrayOf(50f, 50f), 0f),
        CornerPathEffect(BOARD_TERRITORY_RADIUS_CORNERS)
)

