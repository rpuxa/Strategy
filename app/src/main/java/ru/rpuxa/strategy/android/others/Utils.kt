package ru.rpuxa.strategy.android.others

import android.graphics.ComposePathEffect
import android.graphics.PathEffect
import java.util.*

/**
 * Объединение нескольких [PathEffect]
 */
fun composeEffects(effects: Array<out PathEffect>): PathEffect {
    if (effects.isEmpty())
        throw IllegalStateException("effects cant be empty")
    val list = LinkedList<PathEffect>()
    effects.forEach { list.add(it) }
    while (list.size > 1) {
        val a = list.removeAt(0)
        val b = list.removeAt(0)
        list.add(ComposePathEffect(a, b))
    }
    return list[0]
}