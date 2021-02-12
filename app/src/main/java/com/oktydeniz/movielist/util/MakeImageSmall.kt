package com.oktydeniz.movielist.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object ImageSettings {

    fun makeImageSmall(image: Bitmap): Bitmap? {
        val maxSize = 300
        var widht = image.width
        var height = image.height
        val bitmapRatio: Double = widht.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            widht = maxSize
            val scaledHeight = widht / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maxSize
            val scaledHeight = height * bitmapRatio
            widht = scaledHeight.toInt()

        }
        return Bitmap.createScaledBitmap(image, widht, height, true)
    }

    fun bitmapToBytArray(image: Bitmap): ByteArray {
        makeImageSmall(image)
        val outputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 60, outputStream)
        return outputStream.toByteArray()
    }

    fun byteArrayToBitmap(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

}