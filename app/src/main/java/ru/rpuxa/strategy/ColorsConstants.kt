package ru.rpuxa.strategy

import android.graphics.Color
import kotlin.math.sqrt

const val NO_PLAYER_COLOR_CELL = Color.LTGRAY
const val NULL_COLOR_CELL = Color.BLACK

const val CELL_RADIUS = 128f
@JvmField
val CELL_INSIDE_RADIUS = sqrt(3f) / 2 * CELL_RADIUS
const val STROKE_WIDTH = 5f