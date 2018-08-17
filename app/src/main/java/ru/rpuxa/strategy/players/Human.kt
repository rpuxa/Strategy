package ru.rpuxa.strategy.players

import android.view.MotionEvent
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.interfaces.Field
import ru.rpuxa.strategy.field.Location
import ru.rpuxa.strategy.field.interfaces.Buildable
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.field.interfaces.Unit
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

    override fun onMoveStart() {

    }

    override fun onMoveUnit(from: Location, to: Location, sender: Player) {
        visual.animator.animate(MoveUnitAnimation(from, to, field[to].unit, 1000))
        moveMode.off(false)
    }

    override fun onRuleViolate(rule: RuleException) {
        throw rule
    }

    override fun onBuild(buildable: Buildable) {
        visual.draw(field)
    }

    private var lastTouch = null as Array<Float>?
    private var firstTouch = null as Array<Float>?

    fun onTouch(event: MotionEvent) {

        fun Cell.click(chosenObj: Boolean) {
            if (this == Cell.NONE || unit == Unit.NONE || obj == NaturalStructures.EMPTY) {
                ObjInfoController.deactivate(this@Human, true)
                return
            }
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

            if (unit.movePoints > 0)
                ObjInfoController.setInfo(this, this@Human, visual, field, chosenObj)
        }


        fun click(x: Float, y: Float, view: FieldVisualizer) {
            val (worldX, worldY) = view.projectToWorld(x, y)
            var chosenUnit = false
            val findCell = field.find {
                val (cellWorldX, cellWorldY) = view.locationToWorld(it)
                chosenUnit = dist(
                        worldX, worldY,
                        cellWorldX + UNIT_ICON_X,
                        cellWorldY + UNIT_ICON_Y

                ) <= UNIT_ICON_RADIUS
                dist(cellWorldX, cellWorldY, worldX, worldY) <= CELL_INSIDE_RADIUS
            }
            findCell?.click(!chosenUnit) ?: Cell.NONE.click(false)
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