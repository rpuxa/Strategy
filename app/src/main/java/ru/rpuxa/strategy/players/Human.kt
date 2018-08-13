package ru.rpuxa.strategy.players

import android.view.MotionEvent
import ru.rpuxa.strategy.CELL_INSIDE_RADIUS
import ru.rpuxa.strategy.dist
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.visual.FieldView

class Human(override val executor: CommandExecutor, override val field: Field, override val color: Int) : Player {


    private var lastTouch = null as Array<Float>?
    private var firstTouch = null as Array<Float>?

    fun onTouch(fieldView: FieldView, event: MotionEvent) {
        fun Cell.click() {

        }


        fun click(x: Float, y: Float, fieldView: FieldView) {
            val (worldX, worldY) = fieldView.camera.projectToWorld(x, y)
            for ((cellX, cellY, cell) in field.iterator) {
                val (cellWorldX, cellWorldY) = fieldView.getCellCoords(cellX, cellY)
                if (dist(cellWorldX, cellWorldY, worldX, worldY) <= CELL_INSIDE_RADIUS) {
                    cell.click()
                }
            }
        }


        with(fieldView) {
            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    lastTouch = arrayOf(event.x, event.y)
                    firstTouch = arrayOf(event.x, event.y)
                }

                MotionEvent.ACTION_MOVE -> {
                    camera.x += (lastTouch!![0] - event.x) / width * camera.width
                    camera.y += (lastTouch!![1] - event.y) / height * camera.height
                    fieldView.invalidate()
                    lastTouch = arrayOf(event.x, event.y)
                }

                MotionEvent.ACTION_UP -> {
                    if (firstTouch!![0] == event.x && firstTouch!![1] == event.y) {
                        click(event.x, event.y, fieldView)
                    }
                }
            }
        }
    }
}