package ru.rpuxa.strategy.android.visual.view

import ru.rpuxa.strategy.core.implement.visual.animations.*
import ru.rpuxa.strategy.core.interfaces.visual.Animation
import ru.rpuxa.strategy.core.interfaces.visual.FieldAnimator
import ru.rpuxa.strategy.core.others.CELL_RADIUS
import ru.rpuxa.strategy.core.others.pt
import java.util.*

class Animator(override val view: FieldSurfaceView) : FieldAnimator, FieldSurfaceView.Inner {

    override fun animate(animation: Animation) {
        synchronized(animations) {
            animations.addLast(animation)
        }
        startAsyncAnimate()
    }

    private val animations = ArrayDeque<Animation>()

    @Volatile
    private var asyncStarted = false

    private fun startAsyncAnimate() {
        if (asyncStarted)
            return
        asyncStarted = true

        Thread {
            while (true) {
                var empty = false
                var animation: Animation? = null
                synchronized(animations) {
                    if (animations.isEmpty()) {
                        empty = true
                        return@synchronized
                    }
                    animation = animations.pollFirst()
                }

                if (empty) {
                    asyncStarted = false
                    return@Thread
                }
                if (animation!!.async) {
                    Thread {
                        switchAnimation(animation!!)
                    }.start()
                } else
                    switchAnimation(animation!!)
            }
        }.start()
    }

    private fun switchAnimation(animation: Animation) {
        when (animation) {
            is MoveUnitAnimation -> moveUnit(animation)
            is MoveCameraAnimation -> moveCameraToLocation(animation)
            is HealthAnimation -> health(animation)
            is WaitAnimation -> Thread.sleep(animation.duration.toLong())
            is UpdateAnimation -> update(animation)
            is RemoveUnitAnimation -> removeUnit(animation)
            is SeizeTownAnimation -> seizeTown(animation)
            else -> throw UnsupportedOperationException("Animation ${animation.javaClass.name} not supported")
        }
    }

    private fun seizeTown(animation: SeizeTownAnimation) {
        view.fieldObjectsLocation.updateLocations(animation.field)
        view.rebuildTerritories = true
        view.update()
    }

    private fun removeUnit(animation: RemoveUnitAnimation) {
        view.fieldObjectsLocation.remove(animation.unit)
        view.update()
    }

    private fun moveUnit(animation: MoveUnitAnimation) {
        val (worldLocationX, worldLocationY) = view.locationToWorld(animation.to)
        val (worldUnitX, worldUnitY) = view.locationToWorld(animation.from)
        val deltaX = worldLocationX - worldUnitX
        val deltaY = worldLocationY - worldUnitY
        valueAnimate(animation) {
            view.fieldObjectsLocation[animation.unit]!!.offers = deltaX * it pt deltaY * it
            view.update()
        }
        if (animation.updateAfterAnimation)
            view.fieldObjectsLocation.updateLocations(animation.field)
        else
            view.fieldObjectsLocation.zeroShifts()
        view.update()
    }

    private fun moveCameraToLocation(animation: MoveCameraAnimation) {
        val (worldLocationX, worldLocationY) = view.locationToWorld(animation.location)
        val deltaX = worldLocationX - view.camera.x
        val deltaY = worldLocationY - view.camera.y
        val cameraX = view.camera.x
        val cameraY = view.camera.y
        valueAnimate(animation) {
            view.camera.x = cameraX + deltaX * it
            view.camera.y = cameraY + deltaY * it
        }
    }

    private fun health(animation: HealthAnimation) {
        val healthDrawerText = HealthDrawerText(view, animation.health, animation.location)
        view.healthList.add(healthDrawerText)
        valueAnimate(animation) {
            val deltaY = -it * CELL_RADIUS
            healthDrawerText.deltaY = deltaY
            view.update()
        }
        view.healthList.remove(healthDrawerText)
    }

    private fun update(updateAnimation: UpdateAnimation) {
        view.fieldObjectsLocation.updateLocations(updateAnimation.field)
        view.rebuildTerritories = true
        view.update()
    }

    private inline fun valueAnimate(animation: Animation, block: (Float) -> kotlin.Unit) = valueAnimate(animation.duration, block)

    private inline fun valueAnimate(duration: Int, block: (Float) -> kotlin.Unit) {
        val start = System.currentTimeMillis()
        val end = start + duration
        while (true) {
            val currentTime = System.currentTimeMillis()
            if (currentTime >= end)
                break
            val timeGone = currentTime - start
            block(timeGone.toFloat() / duration)
        }
    }
}
