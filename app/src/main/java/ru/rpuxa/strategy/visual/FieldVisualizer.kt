package ru.rpuxa.strategy.visual

import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.geometry.Point
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.visual.view.RegionPaint

interface FieldVisualizer {
    val animator: FieldAnimator
    val cameraWidth: Float
    val cameraHeight: Float
    val width: Float
    val height: Float


    fun draw(field: Field)

    fun select(region: RegionPaint)

    fun unselect(region: RegionPaint)

    fun locationToWorld(location: Location) = locationToWorld(location.x, location.y)

    fun locationToWorld(x: Int, y: Int): Point

    fun setControllingHuman(human: Human)

    fun projectToWorld(x: Float, y: Float): Point

    fun translateCamera(deltaX: Float, deltaY: Float)

    fun invalidate()
}