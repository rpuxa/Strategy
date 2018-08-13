package ru.rpuxa.strategy.field

data class Point(val x: Int,val y: Int)

infix fun Int.pt(y: Int) = Point(this, y)