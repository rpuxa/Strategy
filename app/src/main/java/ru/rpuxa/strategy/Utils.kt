package ru.rpuxa.strategy

import android.graphics.ComposePathEffect
import android.graphics.PathEffect
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.geometry.Line
import ru.rpuxa.strategy.geometry.Point
import java.util.*
import kotlin.math.sqrt

/**
 * Файл утилит проекта
 */

/**
 * Возведение в квадрат
 */
fun Float.sqr() = this * this

/**
 * Расстояние между 2 точками на плоскости
 */
fun dist(x1: Float, y1: Float, x2: Float, y2: Float) = sqrt((x1 - x2).sqr() + (y1 - y2).sqr())


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
fun composeEffectsVararg(vararg effects: PathEffect) = composeEffects(effects)

/**
 * Создаёт новую локацию
 */
infix fun Int.loc(y: Int) = object : Location {
    override var x = this@loc
    override var y = y
}

/**
 * Копирует локацию у объекта
 */
fun Location.copyLocation() = x loc y

/**
 * Создаёт новую линию из 2 точек
 */
infix fun Point.line(p: Point) = Line(this, p)

/**
 * Создаёт новую точки из 2 чисел
 */
infix fun Float.pt(y: Float) = Point(this, y)