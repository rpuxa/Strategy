package ru.rpuxa.strategy.android.visual.view

import android.view.View
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.object_info.view.*
import kotlinx.android.synthetic.main.town_info.view.*
import kotlinx.android.synthetic.main.unit_info.view.*
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.android.visual.TownBuildingsAdapter
import ru.rpuxa.strategy.android.visual.region.PathRegionBuilder
import ru.rpuxa.strategy.core.implement.field.statics.player.Town
import ru.rpuxa.strategy.core.implement.field.units.Colonist
import ru.rpuxa.strategy.core.interfaces.field.Owned
import ru.rpuxa.strategy.core.interfaces.field.objects.FieldObject
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.others.OPEN_OBJ_INFO_DIRECTION

class HeaderInfoController(override val view: FieldSurfaceView) : FieldSurfaceView.Inner {
    private var currentSelection: FieldObject? = null

    fun setHeaderInfo(selectedObject: FieldObject) {
        fun moveModeOn() {
            val regions = PathRegionBuilder(view, view.field!!).createFromUnitMove(selectedObject as Unit)
            regions.forEach(view::select)
            view.human.moveMode.selections = regions
            view.human.moveMode.on(selectedObject)
            view.update()
        }

        val showControlButtons = (selectedObject as? Owned)?.owner?.equals(view.human) ?: true

        val update = currentSelection == selectedObject

        if (update) {
            view.human.moveMode.off(true)
//                moveModeOn()
        }

        if (currentSelection != null)
            view.human.moveMode.off(true)

        currentSelection = selectedObject

        view.activity.main_obj_info.obj_name.text = selectedObject.name
        view.activity.main_obj_info.obj_description.text = selectedObject.description
        when (selectedObject) {
            is Town -> {
                choseMenu(TOWN)
                val panel = view.activity.main_obj_info.town_info_include
                panel.build_stopped.visibility = if (showControlButtons && selectedObject.movesToDestroy > 0)
                    View.VISIBLE
                else
                    View.GONE
                panel.rebellion.visibility = if (selectedObject.movesToDestroy > 0)
                    View.VISIBLE
                else
                    View.GONE
                panel.town_bar_work_points.progress = selectedObject.workPoints
                panel.town_work_points.text = selectedObject.workPoints.toString()
                panel.town_bar_work_points.max = selectedObject.maxWorkPoints
                panel.town_max_work_points.text = selectedObject.maxWorkPoints.toString()
                panel.town_performance.text = selectedObject.performance.toString()
                if (showControlButtons)
                    panel.town_build_list.adapter = TownBuildingsAdapter(view.textures) { info, clazz ->
                        if (info.cost <= selectedObject.workPoints && !selectedObject.bought) {
                            view.human.executor.build(info, clazz, selectedObject, selectedObject, view.human)
                            closeInfoHeader(true)
                        }
                    }
            }

            is Unit -> {
                choseMenu(UNIT)
                val panel = view.activity.main_obj_info.unit_info_include

                panel.build_town.visibility =
                        if (!showControlButtons || selectedObject !is Colonist || selectedObject.movePoints <= 0)
                            View.GONE
                        else
                            View.VISIBLE
                panel.unit_move.visibility = if (!showControlButtons || selectedObject.movePoints == 0) View.GONE else View.VISIBLE
                panel.unit_hp_bar.progress = selectedObject.health
                panel.unit_info_move_point.text = selectedObject.movePoints.toString()
                panel.unit_info_max_move_points.text = selectedObject.baseMovePoints.toString()

                panel.unit_move.setOnClickListener {
                    moveModeOn()
                }

                panel.build_town.setOnClickListener {
                    view.human.executor.layTown(selectedObject, view.human)
                    closeInfoHeader(true)
                }
            }

            else -> throw UnsupportedOperationException("Unknown type ${selectedObject.javaClass.name}")
        }

        if (!update)
            open()
    }

    private val menus = arrayOf(
            R.id.town_info_include,
            R.id.unit_info_include
    )
    private val TOWN = 0
    private val UNIT = 1

    private fun choseMenu(id: Int) {
        val menuId = menus[id]
        menus.forEach {
            view.activity.main_obj_info.findViewById<View>(it).visibility =
                    if (it == menuId)
                        View.VISIBLE
                    else
                        View.GONE
        }
    }

    fun closeInfoHeader(invalidate: Boolean) {
        view.human.moveMode.off(invalidate)
        currentSelection = null
        close()
    }

    private var opened = false
    private fun move(open: Boolean, duration: Long) {
        if (opened == open)
            return
        val view = view.activity.main_obj_info
        view.visibility = if (open) View.VISIBLE else View.GONE
        /* if (open) {
             view.visibility = View.INVISIBLE
         }
          val mainHeight = view.activity.main_view.height
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