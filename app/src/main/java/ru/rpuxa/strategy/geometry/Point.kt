package ru.rpuxa.strategy.geometry

data class Point(val x: Float, val y: Float)

infix fun Float.pt(y: Float) = Point(this, y)