package ru.rpuxa.strategy.android.visual.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import ru.rpuxa.strategy.android.visual.FightDialog
import ru.rpuxa.strategy.core.interfaces.field.Cell
import ru.rpuxa.strategy.core.interfaces.field.objects.units.FightingUnit
import ru.rpuxa.strategy.core.interfaces.field.objects.units.PeacefulUnit
import ru.rpuxa.strategy.core.others.*

internal class Controller(override val view: FieldSurfaceView) : View.OnTouchListener, FieldSurfaceView.Inner {

    private var lastTouch0 = null as Array<Float>?
    private var lastDist: Float? = null
    private var zoomed = false
    private var firstTouch = null as Array<Float>?
    private var human = view.human

    private fun onTouch(event: MotionEvent) {
        fun Cell.click(chosenObj: Boolean) {
            if (human.moveMode.running) {
                when {
                    this in human.moveMode.selections[0] -> {
                        //Обычное передвижение
                        human.executor.moveUnit(human.moveMode.unit, this, human)
                        human.moveMode.on(human.moveMode.unit)
                    }
                    this in human.moveMode.selections[1] -> {
                        //Атака
                        if (unit is PeacefulUnit) {
                            human.executor.moveUnit(human.moveMode.unit, this, human)
                            return
                        }
                        FightDialog(view.context, view.textures, human.moveMode.unit as FightingUnit, unit as FightingUnit) {
                            human.executor.attack(unit, human.moveMode.unit, human)
                        }.show()
                    }
                    else -> {
                        view.headerController.closeInfoHeader(true)
                        return
                    }
                }

                return
            }


            if (this == CELL_NONE || unit == UNIT_NONE && staticObject == STATIC_OBJECT_NONE) {
                view.headerController.closeInfoHeader(true)
                return
            }

            view.openInfoHeader(if (chosenObj && staticObject != STATIC_OBJECT_NONE) staticObject else unit)
        }

        fun click(x: Float, y: Float) {
            val (worldX, worldY) = view.projectToWorld(x, y)
            var chosenUnit = human.moveMode.running
            val findCell = view.field!!.find {
                val (cellWorldX, cellWorldY) = view.locationToWorld(it)
                chosenUnit = dist(
                        worldX, worldY,
                        cellWorldX + UNIT_ICON_X,
                        cellWorldY + UNIT_ICON_Y

                ) <= UNIT_ICON_RADIUS
                dist(cellWorldX, cellWorldY, worldX, worldY) <= CELL_INSIDE_RADIUS
            }
            findCell?.click(!chosenUnit) ?: CELL_NONE.click(false)
        }


        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                lastTouch0 = arrayOf(event.x, event.y)
                firstTouch = arrayOf(event.x, event.y)
                lastDist = null
            }

            MotionEvent.ACTION_MOVE -> when (event.pointerCount) {
                1 -> if (!zoomed) {
                    view.translateCamera(
                            (lastTouch0!![0] - event.x) / view.width * view.camera.width,
                            (lastTouch0!![1] - event.y) / view.height * view.camera.height
                    )
                    lastTouch0 = arrayOf(event.x, event.y)
                }

                2 -> {
                    val currentDist = dist(
                            event.getAxisValue(MotionEvent.AXIS_X, 0),
                            event.getAxisValue(MotionEvent.AXIS_Y, 0),
                            event.getAxisValue(MotionEvent.AXIS_X, 1),
                            event.getAxisValue(MotionEvent.AXIS_Y, 1)
                    )
                    if (lastDist != null) {
                        view.zoomCamera(lastDist!! - currentDist)
                    }

                    lastDist = currentDist

                    zoomed = true
                }
            }

            MotionEvent.ACTION_UP -> {
                if (!zoomed && firstTouch!![0] == event.x && firstTouch!![1] == event.y) {
                    click(event.x, event.y)
                }
                zoomed = false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        onTouch(event!!)
        return true
    }

}