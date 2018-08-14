package ru.rpuxa.strategy.field

interface Unit : Location, Drawable {
    var health: Int
    var movePoints: Int

    val maxMovePoints: Int

    companion object : Fallible {

        val NONE = object : Unit {

            override var movePoints: Int
                get() = fail()
                set(value) = fail()
            override val maxMovePoints: Int
                get() = fail()
            override val x: Int
                get() = fail()
            override val y: Int
                get() = fail()
            override var health: Int
                get() = fail()
                set(value) = fail()
            override val icon: Int
                get() = fail()
        }
    }
}