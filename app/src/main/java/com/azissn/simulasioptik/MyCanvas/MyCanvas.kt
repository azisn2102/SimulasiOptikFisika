package com.azissn.simulasioptik.MyCanvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import com.azissn.simulasioptik.MyModule.SPLDV
import com.azissn.simulasioptik.MyModule.Persamaan
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class MyCanvas(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint()
    private val paintDashed = Paint()
    private val paintRect = Paint()
    private val strokeWidth = 5f
    private var radiusna: Float? = null

    private var coorXValue = 0f
    private var coorYValue = 0f

    private var thinkHight: Float? = null

    private val dashPathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)

    init {
        // Initialize paint properties
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE

        paintDashed.color = Color.BLACK
        paintDashed.strokeWidth = strokeWidth


        paintDashed.pathEffect = dashPathEffect
    }

    fun setCoordinates(x: Float, y: Float) {
        this.coorXValue = x * 6f
        this.coorYValue = y * 6f
        invalidate() // Call to redraw the canvas
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawMain(canvas, coorXValue, coorYValue)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
    }

    private fun drawMain(canvas: Canvas, x: Float, y: Float) {

        paint.strokeWidth = strokeWidth
        paint.textSize = 32f
        radiusna = 300f
        thinkHight = radiusna!! / 3f

        val sBenda = 150f + x
        val hBenda = 300f / 3f
        val rLingkaran = 300f
        val titikFokus = rLingkaran / 2f + y
        val sBayangan = abs(1f / ((1f / titikFokus) - (1f / sBenda)))
        val hBayangan = abs(hBenda * (sBayangan / sBenda))


        paintRect.style = Paint.Style.FILL
        paintRect.color = Color.argb(10, 255, 0, 0)
        canvas.drawRect(
            0f,
            0f,
            width / 2f - 2f * y,
            height.toFloat(),
            paintRect
        )
        paintRect.color = Color.argb(10, 255, 255, 0)
        canvas.drawRect(
            width / 2f - 2f * y,
            0f,
            (width + radiusna!!) / 2f - y,
            height.toFloat(),
            paintRect
        )
        paintRect.color = Color.argb(10, 0, 255, 0)
        canvas.drawRect(
            (width + radiusna!!) / 2f - y,
            0f,
            width / 2f + radiusna!!,
            height.toFloat(),
            paintRect
        )
        paintRect.color = Color.argb(10, 0, 0, 255)
        canvas.drawRect(
            width / 2f + radiusna!!,
            0f,
            width.toFloat(),
            height.toFloat(),
            paintRect
        )

        // garis putus merah (bawah)
        // garis putus datar tinggi benda
        paintDashed.color = Color.rgb(192, 0, 0)
        canvas.drawLine(
            0f,
            height / 2f - thinkHight!!,
            width / 2f + sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) - y,
            height / 2f - thinkHight!!,
            paintDashed
        )
        geoCerminTitik(
            canvas,
            getX((width + radiusna!!) / 2f - y),
            getY(height / 2f),
            getX(width / 2f + sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) - y),
            getY(height / 2f - thinkHight!!),
            paintDashed
        )

        // Garis putus hijau
        paintDashed.color = Color.rgb(0, 192, 0)
        paintDashed.strokeWidth = strokeWidth
        geoCerminTitik(
            canvas,
            getX(width / 2f - 2f * y),
            getY(height / 2f),
            getX((width + radiusna!!) / 2f - x),
            getY(height / 2f - thinkHight!!),
            paintDashed
        )

        // Garis tengah
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, paint)

        // Cermin cekung
        paint.style = Paint.Style.STROKE
        paint.color = Color.rgb(0, 0, 128)
        canvas.drawArc(
            width / 2f - radiusna!! - 4f * y,
            height / 2f - radiusna!! - 2f * y,
            width / 2f + radiusna!!,
            height / 2f + radiusna!! + 2f * y,
            -45f,
            90f,
            false,
            paint
        )

        // Bayangan benda
        val pers1 = Persamaan(
            width / 2f - 2f * y,
            height / 2f,
            (width + radiusna!!) / 2f - x,
            height / 2f - thinkHight!!
        )

        val pers2 = Persamaan(
            (width + radiusna!!) / 2f - y,
            height / 2f,
            width / 2f + sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) - y,
            height / 2f - thinkHight!!
        )

        val intersept = SPLDV(pers1, pers2)
        val presisi: Float = if ((-intersept.b + height / 2f) < 0f) -4f else 4f

        paint.color = Color.rgb(128, 128, 128)
        canvas.drawLine(
            intersept.a,
            height / 2f,
            intersept.a,
            intersept.b,
            paint
        )

        canvas.drawLine(
            intersept.a,
            intersept.b,
            intersept.a + 4f * strokeWidth,
            intersept.b + presisi * strokeWidth,
            paint
        )
        canvas.drawLine(
            intersept.a,
            intersept.b,
            intersept.a - 4f * strokeWidth,
            intersept.b + presisi * strokeWidth,
            paint
        )


        // Menggambar benda asli

        paint.color = Color.BLACK
        canvas.drawLine(
            (width + radiusna!!) / 2f - x,
            height / 2f,
            (width + radiusna!!) / 2f - x,
            height / 2f - thinkHight!!,
            paint
        )

        // Topi benda asli (atas)
        canvas.drawLine(
            (width + radiusna!!) / 2f - x - 4f * strokeWidth,
            height / 2f - thinkHight!! + 4f * strokeWidth,
            (width + radiusna!!) / 2f - x,
            height / 2f - thinkHight!!,
            paint
        )
        canvas.drawLine(
            (width + radiusna!!) / 2f - x,
            height / 2f - thinkHight!!,
            (width + radiusna!!) / 2f - x + 4f * strokeWidth,
            height / 2f - thinkHight!! + 4f * strokeWidth,
            paint
        )

        // Node "F"
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle((width + radiusna!!) / 2f - y, height / 2f, strokeWidth, paint)
        val textF = "F"
        val textWidth = paint.measureText(textF)
        val textHeight = paint.descent() - paint.ascent()
        paint.strokeWidth = 2f
        paint.style = Paint.Style.FILL
        canvas.drawText(
            textF,
            (width + radiusna!! - textWidth) / 2f - y,
            height / 2f + textHeight,
            paint
        )

        // Node "2F"
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(width / 2f - 2f * y, height / 2f, strokeWidth, paint)
        val text2F = "2F"
        val textWidth2 = paint.measureText(text2F)
        val textHeight2 = paint.descent() - paint.ascent()
        paint.strokeWidth = 2f
        paint.style = Paint.Style.FILL
        canvas.drawText(
            "2F",
            (width - textWidth2) / 2f - 2f * y,
            height / 2f + textHeight2,
            paint
        )

        // Text Canvas
        paint.textSize = 24f
        val perbesaran = if ((sBayangan / sBenda).isFinite()) {
            String.format("Perbesaran : %.1f kali", abs(hBayangan / hBenda))
        } else {
            "Tidak ada bayangan"
        }
//        val txtWidthPerbesaran = paint.measureText(perbesaran)
        val txtHeightPerbesaran = paint.descent() - paint.ascent()
        canvas.drawText(
            perbesaran,
            30f,
            height - 6f * txtHeightPerbesaran,
            paint
        )

        paint.textSize = 18f
        val wiKuning: String =
            String.format("2F (R) : %.1f cm", rLingkaran + 2f * y)
        val txtWidthKuning = paint.measureText(wiKuning)
        val txtHeightKuning = paint.descent() - paint.ascent()
        canvas.drawText(
            wiKuning,
            (width - txtWidthKuning) / 2f - 2f * y,
            height - txtHeightKuning,
            paint
        )

        val wiHijau: String =
            String.format("F : %.1f cm", titikFokus)
        val txtWidthHijau = paint.measureText(wiHijau)
        val txtHeightHijau = paint.descent() - paint.ascent()
        canvas.drawText(
            wiHijau,
            ((width + radiusna!!) - txtWidthHijau) / 2f - y,
            height - txtHeightHijau,
            paint
        )

        paint.textSize = 24f
        val wiBenda: String =
            String.format(
                "s : %.1f cm (Benda)",
                sBenda
            )
//        val txtWiWiBenda = paint.measureText(wiBenda)
        val txtHeWiBenda = paint.descent() - paint.ascent()
        canvas.drawText(
            wiBenda,
            30f,
            height - 4f * txtHeWiBenda,
            paint
        )

        val heBenda: String =
            String.format("h : %.1f cm (Benda)", hBenda)
//        val txtWidthBenda = paint.measureText(wiKuning)
        val txtHeightBenda = paint.descent() - paint.ascent()
        canvas.drawText(
            heBenda,
            30f,
            height - 3f * txtHeightBenda,
            paint
        )

        val wiBayangan: String = if (sBayangan.isInfinite()) {
            "Tidak ada bayangan"
        } else {
            String.format("s' : %.1f cm (Bayangan)", sBayangan)
        }

//        val txtWiWiBayangan = paint.measureText(wiBayangan)
        val txtHeWiBayangan = paint.descent() - paint.ascent()
        canvas.drawText(
            wiBayangan,
            30f,
            height - 2f * txtHeWiBayangan,
            paint
        )

        val heBayangan: String = if (hBayangan.isInfinite()) {
            "Tidak ada bayangan"
        } else {
            String.format(
                "h' : %.1f cm (Bayangan)",
                hBayangan
            )
        }
//        val txtWiHeBayangan = paint.measureText(heBayangan)
        val txtHeHeBayangan = paint.descent() - paint.ascent()
        canvas.drawText(
            heBayangan,
            30f,
            height - txtHeHeBayangan,
            paint
        )

    }


    private fun getX(n: Float): Float {
        return n - width / 2f
    }

    private fun getY(n: Float): Float {
        return height / 2f - n
    }

    private fun geoCerminTitik(
        canvas: Canvas,
        x: Float,
        y: Float,
        a: Float,
        b: Float,
        paint: Paint,
        full: Boolean = true
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
