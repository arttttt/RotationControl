package com.arttttt.rotationcontrolv3.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build

object AndroidGraphicsUtils {
    fun applyColorFilterToBitmap(bitmap: Bitmap, color: Int) {
        val paint = Paint().apply {
            colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        }
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    @Suppress("deprecation")
    fun bitmapFromDrawableResource(context: Context, resourceId: Int): Bitmap {
        val drawable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.resources.getDrawable(resourceId, context.theme)
        } else {
            context.resources.getDrawable(resourceId)
        }

        if (drawable is BitmapDrawable) {
            drawable.bitmap?.run {
                return copy(Bitmap.Config.ARGB_8888, true)
            }
        }

        val width = if (drawable.intrinsicWidth > 0 ) drawable.intrinsicWidth else 1
        val height = if (drawable.intrinsicHeight > 0 ) drawable.intrinsicHeight else 1

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            .copy(Bitmap.Config.ARGB_8888, true)
            .apply {
                val canvas = Canvas(this)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
    }
}