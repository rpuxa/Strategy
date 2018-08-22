package ru.rpuxa.strategy.android.visual

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.rpuxa.strategy.R

/**
 * Хранилище текстур для android
 */
class TextureBank(private val resources: Resources) {
    private val list = ArrayList<Bitmap>()
    private val scaledList = ArrayList<Scaled>()

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


    init {
        arr.forEach { list.add(BitmapFactory.decodeResource(resources, it)) }
    }

    companion object {
        val arr = arrayOf(
                R.drawable.unit,
                R.drawable.sword,
                R.drawable.flag,
                R.drawable.peaceful_unit,
                R.drawable.town,
                R.drawable.seized_town,
                R.drawable.capital
        )
    }


    private inner class Scaled(val bitmap: Bitmap, val width: Int, val height: Int, val index: Int)
}