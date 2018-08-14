package ru.rpuxa.strategy

import android.graphics.ComposePathEffect
import android.graphics.PathEffect
import java.util.*
import kotlin.math.sqrt

fun Float.sqr() = this * this

fun dist(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2).sqr() + (y1 - y2).sqr())

fun composeEffects(effects: Array<out PathEffect>): PathEffect {
    if (effects.isEmpty())
        throw IllegalStateException("effects cant be empty")
    val list = LinkedList<PathEffect>()
    effects.forEach { list.add(it) }
    while (effects.size > 1) {
        val a = list.removeAt(0)
        val b = list.removeAt(0)
        list.add(ComposePathEffect(a, b))
    }

    return list[0]
}

