package ru.rpuxa.strategy.android.visual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.town_building.view.*
import ru.rpuxa.strategy.R
import ru.rpuxa.strategy.core.interfaces.field.objects.Buildable
import ru.rpuxa.strategy.core.interfaces.field.objects.units.Unit
import ru.rpuxa.strategy.core.implement.field.units.Colonist

class TownBuildingsAdapter(
        private val textureBank: TextureBank,
        private val onBuy: (Buildable) -> kotlin.Unit
) : BaseAdapter() {

    private var inflater: LayoutInflater? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (inflater == null)
            inflater = LayoutInflater.from(parent!!.context)
        val view = convertView
                ?: inflater!!.inflate(R.layout.town_building, parent, false)
        val building = townBuilders[position]
        view.name.text = building.name
        view.description.text = building.description
        view.cost.text = building.cost.toString()
        view.buy.text = if (building is Unit) "нанять" else "построить"
        view.icon.setImageBitmap(textureBank[building.icon])
        view.buy.setOnClickListener {
            onBuy(building)
        }
        return view
    }

    override fun getItem(position: Int) = townBuilders[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = townBuilders.size
}

val townBuilders = arrayOf<Buildable>(
        Colonist(-1, -1)
)