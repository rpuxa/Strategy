package ru.rpuxa.strategy.geometry

data class Line(val from: Point, val to: Point)

infix fun Point.line(p: Point) = Line(this, p)