package ru.rpuxa.strategy

import kotlin.math.sqrt

fun Float.sqr() = this * this

fun dist(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2).sqr() + (y1 - y2).sqr())