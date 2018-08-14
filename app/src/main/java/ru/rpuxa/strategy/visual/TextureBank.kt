package ru.rpuxa.strategy.visual

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.rpuxa.strategy.R

class TextureBank(private val resources: Resources) {
    private val list = ArrayList<Bitmap>()
    private val scaledList = ArrayList<Scaled>()
    var lastSize = -1L


    init {
        val arr = arrayOf(
                R.drawable.unit,
                R.drawable.sword
        )

        arr.forEach { list.add(BitmapFactory.decodeResource(resources, it)) }
    }

    operator fun get(i: Int) = list[i]

    fun scale(index: Int, width: Int, height: Int): Bitmap {
        val find = scaledList.find { it.width == width && it.height == height }
        if (find == null) {
            val bitmap = Bitmap.createScaledBitmap(this[index], width, height, false)
            scaledList.add(Scaled(bitmap, width, height))
            return bitmap
        }
        return find.bitmap
    }

    fun updateSize(width: Int, height: Int) {
        val size = (width.toLong() shl 32) or height.toLong()
        if (size != lastSize)
            scaledList.clear()
        lastSize = size
    }

    companion object {
        val UNIT = 0
        val SWORD = 1
    }


    private inner class Scaled(val bitmap: Bitmap, val width: Int, val height: Int) {

    }
}