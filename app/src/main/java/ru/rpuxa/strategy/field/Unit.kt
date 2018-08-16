package ru.rpuxa.strategy.field

interface Unit : Location, Drawable {
    var health: Int
    var movePoints: Int

    val maxMovePoints: Int
    val name: String
    val description: String

    companion object : Fallible {

        val NONE = object : Unit {
            override val description: String
                get() = fail()
            override val name: String
                get() = fail()
            override var movePoints: Int
                get() = fail()
                set(_) = fail()
            override val maxMovePoints: Int
                get() = fail()
            override var x: Int
                get() = fail()
                set(_) = fail()
            override var y: Int
                get() = fail()
                set(_) = fail()
            override var health: Int
                get() = fail()
                set(_) = fail()
            override val icon: Int
                get() = fail()
        }
    }
}