package ru.rpuxa.strategy.field

interface Location {
    val x: Int
    val y: Int
}

infix fun Int.loc(y: Int) = object : Location {
    override val x: Int
        get() = this@loc
    override val y: Int
        get() = y

}