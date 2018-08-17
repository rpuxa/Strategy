package ru.rpuxa.strategy.visual.view

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.rpuxa.strategy.R

class TextureBank(private val resources: Resources) {
    private val list = ArrayList<Bitmap>()
    private val scaledList = ArrayList<Scaled>()
    private var lastSize = -1L

    init {
        instance = this
    }


    operator fun get(i: Int) = list[i]

    fun getScaled(index: Int, width: Int, height: Int): Bitmap {
        val find = scaledList.find { it.width == width && it.height == height && it.index == index }
        if (find == null) {
            val bitmap = Bitmap.createScaledBitmap(this[index], width, height, false)
            scaledList.add(Scaled(bitmap, width, height, index))
            return bitmap
        }
        return find.bitmap
    }

    fun getProportionalScaled(index: Int, height: Int): Bitmap {
        val bitmap = list[index]
        val width = bitmap.width.toFloat() / bitmap.height * height
        return getScaled(index, width.toInt(), height)
    }

    fun updateSize(width: Int, height: Int) {
        val size = (width.toLong() shl 32) or height.toLong()
        if (size != lastSize)
            scaledList.clear()
        lastSize = size
    }


    init {
        arr.forEach { list.add(BitmapFactory.decodeResource(resources, it)) }
    }

    companion object {
        lateinit var instance: TextureBank
        val arr = arrayOf(
                R.drawable.unit,
                R.drawable.sword,
                R.drawable.flag,
                R.drawable.peaceful_unit,
                R.drawable.town
        )
        const val UNIT = 0
        const val SWORD = 1
        const val FLAG = 2
        const val PEACEFUL_UNIT = 3
        const val TOWN = 4
    }


    private inner class Scaled(val bitmap: Bitmap, val width: Int, val height: Int, val index: Int)
}