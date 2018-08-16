package ru.rpuxa.strategy.players

import android.view.MotionEvent
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.visual.FieldVisualizer
import ru.rpuxa.strategy.visual.animations.MoveUnitAnimation
import ru.rpuxa.strategy.visual.view.ObjInfoController
import ru.rpuxa.strategy.visual.view.RegionPaint

class Human(override val executor: CommandExecutor,
            override val field: Field,
            override val color: Int,
            val visual: FieldVisualizer) : Player {

    override fun onStart() {
        visual.draw(field)
    }

    override fun onMoveUnit(from: Location, to: Location, sender: Player) {
        visual.animator.animate(MoveUnitAnimation(from, to, field[to].unit, 1000))
        moveMode.off(false)
    }

    override fun onRuleViolate(rule: RuleException) {
        throw rule
    }

    private var lastTouch = null as Array<Float>?
    private var firstTouch = null as Array<Float>?

    fun onTouch(event: MotionEvent) {

        fun Cell.click() {
            if (moveMode.running) {
                if (this in moveMode.selection) {
                    executor.moveUnit(moveMode.unit, this, this@Human)
                    if (moveMode.unit.movePoints == 0)
                        ObjInfoController.deactivate(this@Human, false)
                    return
                }
                ObjInfoController.deactivate(this@Human, true)
                return
            }
            ObjInfoController.setInfo(this, this@Human, visual, field)
        }


        fun click(x: Float, y: Float, view: FieldVisualizer) {
            val (worldX, worldY) = view.projectToWorld(x, y)
            field.find {
                val (cellWorldX, cellWorldY) = view.locationToWorld(it)
                dist(cellWorldX, cellWorldY, worldX, worldY) <= CELL_INSIDE_RADIUS
            }?.click() ?: Cell.NONE.click()
        }
        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                lastTouch = arrayOf(event.x, event.y)
                firstTouch = arrayOf(event.x, event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                visual.translateCamera((lastTouch!![0] - event.x) / visual.width * visual.cameraWidth, (lastTouch!![1] - event.y) / visual.height * visual.cameraHeight)
                lastTouch = arrayOf(event.x, event.y)
            }

            MotionEvent.ACTION_UP -> {
                if (firstTouch!![0] == event.x && firstTouch!![1] == event.y) {
                    click(event.x, event.y, visual)
                }
            }
        }
    }

    val moveMode = MoveMode()

    inner class MoveMode internal constructor() {

        lateinit var unit: Unit
        lateinit var selection: RegionPaint
        var running = false

        fun on(unit: Unit) {
            this.unit = unit
            running = true
        }

        fun off(invalidate: Boolean) {
            if (!running)
                return
            visual.unselect(selection)
            if (invalidate)
                visual.invalidate()
            running = false
        }
    }
}