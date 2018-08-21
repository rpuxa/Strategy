package ru.rpuxa.strategy.android.visual

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint

class IconsBank(private val textures: TextureBank) {
    private val map = HashMap<Long, Bitmap>()
    private val paint = Paint()

    operator fun get(id: Int, color: Int, radius: Int): Bitmap {
        val key = (id.toLong() shl 32) or color.toLong()
        val bitmap = map[key]
        if (bitmap != null)
            return bitmap
        val diameter = 2 * radius
        val scaled = Bitmap.createScaledBitmap(textures[id], diameter, diameter, false)
        val result = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        paint.color = color
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint)
        canvas.drawBitmap(scaled, 0f, 0f, paint)
        map[key] = result
        return result
    }
}