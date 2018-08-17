package ru.rpuxa.strategy.visual.view

import android.animation.ValueAnimator
import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import ru.rpuxa.strategy.OPEN_OBJ_INFO_DIRECTION
import ru.rpuxa.strategy.UNIT_REGION_MOVE_BORDER_COLOR
import ru.rpuxa.strategy.UNIT_REGION_MOVE_BORDER_EFFECTS
import ru.rpuxa.strategy.UNIT_REGION_MOVE_BORDER_WIDTH
import ru.rpuxa.strategy.activities.GameActivity
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.Field
import ru.rpuxa.strategy.field.Unit
import ru.rpuxa.strategy.field.interfaces.NaturalStructures
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.visual.FieldVisualizer

object ObjInfoController {
    private lateinit var activity: GameActivity
    private var selectedUnit: Unit? = null

    fun setGameActivity(activity: GameActivity) {
        ObjInfoController.activity = activity
    }

    fun setInfo(cell: Cell, human: Human, visual: FieldVisualizer, field: Field) {
        val unit = cell.unit
        if (selectedUnit == unit)
            return
        if (cell == Cell.NONE || cell.obj == NaturalStructures.EMPTY && cell.unit == Unit.NONE) {
            deactivate(human, true)
            return
        }
        val view = activity.main_obj_info
        if (unit != Unit.NONE) {
            if (selectedUnit != null) {
                human.moveMode.off(true)
            }
            selectedUnit = cell.unit
            view.obj_name.text = unit.name
            view.obj_description.text = unit.description
            view.obj_hp_bar.progress = unit.health
            view.obj_move.visibility = View.VISIBLE

            view.obj_move.setOnClickListener {
                val selection = RegionBuilder(visual, field).createFromUnitMove(unit)
                        .board(UNIT_REGION_MOVE_BORDER_COLOR)
                        .effects(UNIT_REGION_MOVE_BORDER_EFFECTS)
                        .boardWidth(UNIT_REGION_MOVE_BORDER_WIDTH)
                visual.select(selection)
                human.moveMode.selection = selection
                human.moveMode.on(unit)
                visual.invalidate()
            }

            open()
        } else {
            deactivate(human, true)
        }
    }

    fun deactivate(human: Human, invalidate: Boolean) {
        human.moveMode.off(invalidate)
        selectedUnit = null
        close()
    }

    private var opened = false
    fun move(open: Boolean, duration: Long) {
        if (opened == open)
            return
        val view = activity.main_obj_info
        val first = if (open) 0 else -view.height
        val second = if (open) -view.height else 0
        val animation = ValueAnimator.ofInt(first, second)
        animation.duration = duration
        animation.addUpdateListener {
            val shift = it.animatedValue as Int
            view.layout(0, activity.main_view.height + shift , view.width, activity.main_view.height + view.height + shift)
        }
        animation.start()
        opened = open
    }

    fun open() = move(true, OPEN_OBJ_INFO_DIRECTION)

    fun close() = move(false, OPEN_OBJ_INFO_DIRECTION)
}