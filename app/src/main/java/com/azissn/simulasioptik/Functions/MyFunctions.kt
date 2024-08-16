package com.azissn.simulasioptik.Functions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

class MyFunctions {

    fun getX(width: Float, n: Float): Float {
        return n - width / 2f
    }

    fun getY(height: Float, n: Float): Float {
        return height / 2f - n
    }

    fun geoCerminTitik(
        canvas: Canvas,
        x: Float,
        y: Float,
        a: Float,
        b: Float,
        paint: Paint,
        width: Float,
        height: Float,
        full: Boolean = true,
    ) {
        val startX: Float
        val startY: Float
        val endX1: Float
        val endY1: Float
        val endX2: Float
        val endY2: Float

        val xAdjusted = x + width / 2f
        val yAdjusted = -y + height / 2f
        val aAdjusted = a + width / 2f
        val bAdjusted = -b + height / 2f

        val coor = PointF(
            -xAdjusted + 2 * aAdjusted,
            -yAdjusted + 2 * bAdjusted
        )

        val coorb = PointF(
            -aAdjusted + 2 * xAdjusted,
            -bAdjusted + 2 * yAdjusted
        )

        val coor2 = PointF(
            -xAdjusted + 2 * coor.x,
            -yAdjusted + 2 * coor.y
        )

        val coorb2 = PointF(
            -aAdjusted + 2 * coorb.x,
            -bAdjusted + 2 * coorb.y
        )

        val coor3 = PointF(
            -xAdjusted + 2 * coor2.x,
            -yAdjusted + 2 * coor2.y
        )

        val coorb3 = PointF(
            -aAdjusted + 2 * coorb2.x,
            -bAdjusted + 2 * coorb2.y
        )

        val coor4 = PointF(
            -xAdjusted + 2 * coor3.x,
            -yAdjusted + 2 * coor3.y
        )

        val coorb4 = PointF(
            -aAdjusted + 2 * coorb3.x,
            -bAdjusted + 2 * coorb3.y
        )

        startX = xAdjusted
        startY = yAdjusted

        if (full) {
            endX1 = coor4.x
            endY1 = coor4.y
            endX2 = coorb4.x
            endY2 = coorb4.y
        } else {
            endX1 = aAdjusted
            endY1 = bAdjusted
            endX2 = coorb4.x
            endY2 = coorb4.y
        }

        // Menggambar garis pertama
//        color = warna
        canvas.drawLine(startX, startY, endX1, endY1, paint)
//
//        // Menggambar garis kedua
        canvas.drawLine(startX, startY, endX2, endY2, paint)
    }
}