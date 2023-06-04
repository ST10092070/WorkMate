package com.opsc.workmate.data

import android.graphics.BitmapFactory
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayInputStream

class Image {
    companion object {
        fun setBase64Image(base64String: String?, imageView: ImageView) {
            try {
                if (base64String.isNullOrEmpty()) {
                    // Set the image to gray if the base64String is null or empty
                    val grayScaleColorMatrix = ColorMatrix().apply {
                        setSaturation(0f)
                    }
                    val grayScaleFilter = ColorMatrixColorFilter(grayScaleColorMatrix)
                    imageView.colorFilter = grayScaleFilter
                    imageView.setImageResource(android.R.color.transparent)
                } else {
                    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                    val inputStream = ByteArrayInputStream(decodedBytes)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    imageView.setImageBitmap(bitmap)
                    imageView.colorFilter = null // Remove any existing color filter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Set the image to gray if an exception occurs during decoding
                val grayScaleColorMatrix = ColorMatrix().apply {
                    setSaturation(0f)
                }
                val grayScaleFilter = ColorMatrixColorFilter(grayScaleColorMatrix)
                imageView.colorFilter = grayScaleFilter
                imageView.setImageResource(android.R.color.transparent)
            }
        }
    }
}
