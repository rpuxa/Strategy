package ru.rpuxa.strategy.android.visual.view

import ru.rpuxa.strategy.core.geometry.Point
import ru.rpuxa.strategy.core.interfaces.field.Field
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.others.isNotNone
import ru.rpuxa.strategy.core.others.pt
import java.util.concurrent.ConcurrentHashMap

class FieldObjectsLocation(override val view: FieldSurfaceView) :
        ConcurrentHashMap<FieldObject, FieldObjectsLocation.Shift>(), FieldSurfaceView.Inner {

    fun updateLocations(field: Field) {
        view.field = field
        clear()
        for (cell in field)
            for (obj in cell.objects)
                if (obj.isNotNone) {
                    val locationToWorld = view.locationToWorld(obj)
                    val value = locationToWorld.toShift()
                    put(obj, value)
                }
    }

    inner class Shift(var start: Point, var offers: Point) {
        fun toPoint() = start.x + offers.x pt start.y + offers.y

        fun zeroOffers() {
            start = toPoint()
            offers = 0f pt 0f
        }
    }

    fun Point.toShift() = Shift(this, 0f pt 0f)

    fun zeroShifts() {
        for (shift in values)
            shift.zeroOffers()
    }
}
