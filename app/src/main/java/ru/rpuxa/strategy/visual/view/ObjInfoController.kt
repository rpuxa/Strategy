package ru.rpuxa.strategy.visual.view

import android.animation.ValueAnimator
import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import kotlinx.android.synthetic.main.town_info.view.*
import kotlinx.android.synthetic.main.unit_info.view.*
import ru.rpuxa.strategy.*
import ru.rpuxa.strategy.activities.GameActivity
import ru.rpuxa.strategy.field.Cell
import ru.rpuxa.strategy.field.copyLocation
import ru.rpuxa.strategy.field.interfaces.*
import ru.rpuxa.strategy.field.interfaces.Unit
import ru.rpuxa.strategy.field.objects.player.Town
import ru.rpuxa.strategy.field.units.Colonist
import ru.rpuxa.strategy.players.Human
import ru.rpuxa.strategy.visual.FieldVisualizer

object ObjInfoController {
    private lateinit var activity: GameActivity
    private var currentSelection: FieldObject? = null

    fun setGameActivity(activity: GameActivity) {
        ObjInfoController.activity = activity
        activity.main_obj_info.visibility = View.GONE
    }

    fun setInfo(cell: Cell, human: Human, visual: FieldVisualizer, field: Field, choseObj: Boolean, update: Boolean) {
        val selection: FieldObject = when {
            cell.obj != StaticObject.EMPTY && (choseObj || cell.unit == Unit.NONE) -> cell.obj
            cell.unit != Unit.NONE -> cell.unit
            else -> throw IllegalStateException("Cannot set empty cell")
        }

        fun moveModeOn() {
            val regionPaint = RegionBuilder(visual, field).createFromUnitMove(selection as Unit)
                    .board(UNIT_REGION_MOVE_BORDER_COLOR)
                    .effects(UNIT_REGION_MOVE_BORDER_EFFECTS)
                    .boardWidth(UNIT_REGION_MOVE_BORDER_WIDTH)
            visual.select(regionPaint)
            human.moveMode.selection = regionPaint
            human.moveMode.on(selection)
            visual.invalidate()
        }

        if (update) {
            human.moveMode.off(true)
            currentSelection = null
            moveModeOn()
        }

        if (currentSelection == selection)
            return
        if (currentSelection != null)
            human.moveMode.off(true)

        currentSelection = selection

        activity.main_obj_info.obj_name.text = selection.name
        activity.main_obj_info.obj_description.text = selection.description
        when (selection) {
            is Town -> {
                choseMenu(TOWN)
                val view = activity.main_obj_info.town_info_include
                view.town_bar_work_points.progress = selection.workPoints
                view.town_work_points.text = selection.workPoints.toString()
                view.town_bar_work_points.max = selection.maxWorkPoints
                view.town_max_work_points.text = selection.maxWorkPoints.toString()
                view.town_performance.text = selection.performance.toString()
                view.town_build_list.adapter = TownBuildingsAdapter {
                    if (it.cost <= selection.workPoints && !selection.bought) {
                        it.x = selection.x
                        it.y = selection.y
                        human.executor.build(it, selection, human)
                        deactivate(human, true)
                    }
                }
            }
            is Unit -> {
                choseMenu(UNIT)
                val view = activity.main_obj_info.unit_info_include

                view.build_town.visibility =
                        if (selection is Colonist && selection.movePoints > 0)
                            View.VISIBLE
                        else
                            View.GONE
                view.unit_hp_bar.progress = selection.health
                view.unit_move.visibility = if (selection.movePoints == 0) View.GONE else View.VISIBLE
                view.unit_info_move_point.text = selection.movePoints.toString()
                view.unit_info_max_move_points.text = selection.maxMovePoints.toString()

                view.unit_move.setOnClickListener {
                    moveModeOn()
                }

                view.build_town.setOnClickListener {
                    human.executor.layTown(selection, human)
                    deactivate(human, true)
                }
            }
            else -> throw UnsupportedOperationException("Unknown type ${selection.javaClass.name}")
        }

        activity.main_obj_info.obj_close.setOnClickListener {
            deactivate(human, true)
        }
        if (!update)
            open()
    }

    private val menus = arrayOf(
            R.id.town_info_include,
            R.id.unit_info_include
    )
    private const val TOWN = 0
    private const val UNIT = 1

    private fun choseMenu(id: Int) {
        val menuId = menus[id]
        menus.forEach {
            activity.main_obj_info.findViewById<View>(it).visibility =
                    if (it == menuId)
                        View.VISIBLE
                    else
                        View.GONE
        }
    }

    fun deactivate(human: Human, invalidate: Boolean) {
        human.moveMode.off(invalidate)
        currentSelection = null
        close()
    }

    private var opened = false
    private fun move(open: Boolean, duration: Long) {
        if (opened == open)
            return
        val view = activity.main_obj_info
        view.visibility = if (open) View.VISIBLE else View.GONE
       /* if (open) {
            view.visibility = View.INVISIBLE
        }
         val mainHeight = activity.main_view.height
         val first = if (open) 0 else -view.height
         val second = if (open) -view.height else 0
         val animation = ValueAnimator.ofInt(first, second)
         animation.duration = duration
         animation.addUpdateListener {
             if (open && view.is)
             val shift = it.animatedValue as Int
             val i = mainHeight + shift

             if (i >= 0) {
                 val width = view.height
                 view.layout(0, i, view.width, mainHeight + view.height + shift)
             }
         }
         animation.start()*/
        opened = open
    }

    fun open() = move(true, OPEN_OBJ_INFO_DIRECTION)

    fun close() = move(false, OPEN_OBJ_INFO_DIRECTION)
}