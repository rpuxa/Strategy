package ru.rpuxa.strategy.field

interface Location {
    var x: Int
    var y: Int
}

infix fun Int.loc(y: Int) = object : Location {
    override var x = this@loc
    override var y = y
}

fun Location.copyLocation() = x loc y