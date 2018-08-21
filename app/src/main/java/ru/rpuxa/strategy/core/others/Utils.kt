package ru.rpuxa.strategy.core.others

import ru.rpuxa.strategy.core.geometry.Line
import ru.rpuxa.strategy.core.geometry.Point
import ru.rpuxa.strategy.core.interfaces.field.Location
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.field.objects.statics.StaticObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
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
 * Создаёт новую локацию
 */
infix fun Int.loc(y: Int) = object : Location {
    override var x = this@loc
    override var y = y
    override fun copy() = fail()
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

val FieldObject.isNone: Boolean
    get() = when (this) {
        is Unit -> this === UNIT_NONE
        is StaticObject -> this === STATIC_OBJECT_NONE
        else -> throw Exception()
    }

val FieldObject.isNotNone: Boolean
    get() = !isNone
