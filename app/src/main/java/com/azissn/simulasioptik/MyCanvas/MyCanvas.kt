package com.azissn.simulasioptik.MyCanvas

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.azissn.simulasioptik.Functions.MyFunctions
import com.azissn.simulasioptik.MyModule.SPLDV
import com.azissn.simulasioptik.MyModule.Persamaan
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class MyCanvas(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private lateinit var myFunctions: MyFunctions
    private val paint = Paint()
    private val paintDashed = Paint()
    private val paintRect = Paint()
    private val strokeWidth = 5f
    private var radiusna: Float? = null
    private var asalCermin: String = "Cermin Cekung"

    private var coorXValue = 0f
    private var coorYValue = 0f

    private var thinkHight: Float? = null
    private var startAngle = -45f
    private var sweepAngle = 90f

    private val animatorCermin = ObjectAnimator.ofFloat(this, "arcProgress", 0f, 1f).apply {
        duration = 1000 // Durasi animasi
        addUpdateListener {
            invalidate() // Memperbarui tampilan saat animasi
        }
    }

    private val animatorLatarMerah = ObjectAnimator.ofFloat(this, "geserLatarMerah", 0f, 1f).apply {
        duration = 1000
        addUpdateListener {
            invalidate()
        }
    }

    // Setter untuk nilai animasi
    var arcProgress: Float
        get() = 0f
        set(value) {
            startAngle = if (asalCermin == "Cermin Cekung") {
                135f - 180f * value
            } else {
                -45 - 180f * value
            }
            sweepAngle = 90f
        }

    var geserLatarMerah: Float
        get() = 0f
        set(value) {
            coorXValue = value
            invalidate()
        }

    private val dashPathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)

    init {
        // Initialize paint properties
        myFunctions = MyFunctions()
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


    fun gantiCermin() {
        asalCermin = if (asalCermin == "Cermin Cekung") {
            "Cermin Cembung"
        } else {
            "Cermin Cekung"
        }
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

        val sBenda = if (asalCermin == "Cermin Cekung") {
            150f + x
        } else {
            300f + x
        }
        val hBenda = 300f / 3f
        val rLingkaran = 300f
        val titikFokus = rLingkaran / 2f + y
        val sBayangan = if (asalCermin == "Cermin Cekung") {
            abs(1f / ((1f / titikFokus) - (1f / sBenda)))
        } else {
            abs(1f / ((1f / titikFokus) + (1f / sBenda)))
        }
        val hBayangan = abs(hBenda * (sBayangan / sBenda))


        paintRect.style = Paint.Style.FILL
        paintRect.color = Color.argb(10, 255, 0, 0)
        if (asalCermin == "Cermin Cekung") {
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
        } else {
            canvas.drawRect(
                0f,
                0f,
                width / 2f - radiusna!!,
                height.toFloat(),
                paintRect
            )
            paintRect.color = Color.argb(10, 255, 255, 0)
            canvas.drawRect(
                width / 2f - radiusna!!,
                0f,
                (width - radiusna!!) / 2f + y,
                height.toFloat(),
                paintRect
            )
            paintRect.color = Color.argb(10, 0, 255, 0)
            canvas.drawRect(
                (width - radiusna!!) / 2f + y,
                0f,
                width / 2f + 2f * y,
                height.toFloat(),
                paintRect
            )
            paintRect.color = Color.argb(10, 0, 0, 255)
            canvas.drawRect(
                width / 2f + 2f * y,
                0f,
                width.toFloat(),
                height.toFloat(),
                paintRect
            )
        }


        // garis putus merah (bawah)
        // garis putus datar tinggi benda
        paintDashed.color = Color.rgb(192, 0, 0)
        if (asalCermin == "Cermin Cekung") {
            canvas.drawLine(
                0f,
                height / 2f - thinkHight!!,
                width / 2f + sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) - y,
                height / 2f - thinkHight!!,
                paintDashed
            )
            myFunctions.geoCerminTitik(
                canvas,
                myFunctions.getX(width.toFloat(), (width + radiusna!!) / 2f - y),
                myFunctions.getY(height.toFloat(), height / 2f),
                myFunctions.getX(
                    width.toFloat(),
                    width / 2f + sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) - y
                ),
                myFunctions.getY(height.toFloat(), height / 2f - thinkHight!!),
                paintDashed,
                width.toFloat(),
                height.toFloat()
            )

            // Garis putus hijau
            paintDashed.color = Color.rgb(0, 192, 0)
            paintDashed.strokeWidth = strokeWidth
            myFunctions.geoCerminTitik(
                canvas,
                myFunctions.getX(width.toFloat(), width / 2f - 2f * y),
                myFunctions.getY(height.toFloat(), height / 2f),
                myFunctions.getX(width.toFloat(), (width + radiusna!!) / 2f - x),
                myFunctions.getY(height.toFloat(), height / 2f - thinkHight!!),
                paintDashed,
                width.toFloat(),
                height.toFloat()
            )
        } else {
            canvas.drawLine(
                0f,
                height / 2f - thinkHight!!,
                width / 2f - sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) + y,
                height / 2f - thinkHight!!,
                paintDashed
            )
            myFunctions.geoCerminTitik(
                canvas,
                myFunctions.getX(width.toFloat(), (width - radiusna!!) / 2f + y),
                myFunctions.getY(height.toFloat(), height / 2f),
                myFunctions.getX(
                    width.toFloat(),
                    width / 2f - sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) + y
                ),
                myFunctions.getY(height.toFloat(), height / 2f - thinkHight!!),
                paintDashed,
                width.toFloat(),
                height.toFloat()
            )

            // Garis putus hijau
            paintDashed.color = Color.rgb(0, 192, 0)
            paintDashed.strokeWidth = strokeWidth
            myFunctions.geoCerminTitik(
                canvas,
                myFunctions.getX(width.toFloat(), width / 2f + y),
                myFunctions.getY(height.toFloat(), height / 2f),
                myFunctions.getX(width.toFloat(), (width - 4f * radiusna!!) / 2f - x),
                myFunctions.getY(height.toFloat(), height / 2f - thinkHight!!),
                paintDashed,
                width.toFloat(),
                height.toFloat()
            )
        }

        // Garis tengah
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, paint)

        paint.style = Paint.Style.STROKE
        paint.color = Color.rgb(0, 0, 128)

        val left = if (asalCermin == "Cermin Cekung") {
            width / 2f - radiusna!! - 4f * y
        } else {
            width / 2f - radiusna!!
        }
        val top = if (asalCermin == "Cermin Cekung") {
            height / 2f - radiusna!! - 2f * y
        } else {
            height / 2f - radiusna!! - 2f * y
        }
        val right = if (asalCermin == "Cermin Cekung") {
            width / 2f + radiusna!!
        } else {
            width / 2f + radiusna!! + 4f * y
        }
        val bottom = if (asalCermin == "Cermin Cekung") {
            height / 2f + radiusna!! + 2f * y
        } else {
            height / 2f + radiusna!! + 2f * y
        }

        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, paint)

        // Bayangan benda
        if (asalCermin == "Cermin Cekung") {
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

        } else {
            val pers1 = Persamaan(
                width / 2f + y,
                height / 2f,
                (width - 4f * radiusna!!) / 2f - x,
                height / 2f - thinkHight!!
            )

            val pers2 = Persamaan(
                (width - radiusna!!) / 2f + y,
                height / 2f,
                width / 2f - sqrt((radiusna!! + y).pow(2) - thinkHight!!.pow(2)) + y,
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

        }

        // Menggambar benda asli
        if (asalCermin == "Cermin Cekung") {
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
        } else {
            paint.color = Color.BLACK
            canvas.drawLine(
                (width - 4f * radiusna!!) / 2f - x,
                height / 2f,
                (width - 4f * radiusna!!) / 2f - x,
                height / 2f - thinkHight!!,
                paint
            )

            // Topi benda asli (atas)
            canvas.drawLine(
                (width - 4f * radiusna!!) / 2f - x - 4f * strokeWidth,
                height / 2f - thinkHight!! + 4f * strokeWidth,
                (width - 4f * radiusna!!) / 2f - x,
                height / 2f - thinkHight!!,
                paint
            )
            canvas.drawLine(
                (width - 4f * radiusna!!) / 2f - x,
                height / 2f - thinkHight!!,
                (width - 4f * radiusna!!) / 2f - x + 4f * strokeWidth,
                height / 2f - thinkHight!! + 4f * strokeWidth,
                paint
            )
        }


        // Node "F"
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL_AND_STROKE
        if (asalCermin == "Cermin Cekung") {
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
            val text2F = "R"
            val textWidth2 = paint.measureText(text2F)
            val textHeight2 = paint.descent() - paint.ascent()
            paint.strokeWidth = 2f
            paint.style = Paint.Style.FILL
            canvas.drawText(
                "R",
                (width - textWidth2) / 2f - 2f * y,
                height / 2f + textHeight2,
                paint
            )

        } else {
            canvas.drawCircle((width - radiusna!!) / 2f + y, height / 2f, strokeWidth, paint)
            val textF = "F"
            val textWidth = paint.measureText(textF)
            val textHeight = paint.descent() - paint.ascent()
            paint.strokeWidth = 2f
            paint.style = Paint.Style.FILL
            canvas.drawText(
                textF,
                (width - radiusna!! - textWidth) / 2f + y,
                height / 2f + textHeight,
                paint
            )

            // Node "2F"
            paint.strokeWidth = strokeWidth
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawCircle(width / 2f + 2f * y, height / 2f, strokeWidth, paint)
            val text2F = "R"
            val textWidth2 = paint.measureText(text2F)
            val textHeight2 = paint.descent() - paint.ascent()
            paint.strokeWidth = 2f
            paint.style = Paint.Style.FILL
            canvas.drawText(
                "R",
                (width - textWidth2) / 2f + 2f * y,
                height / 2f + textHeight2,
                paint
            )

        }


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

        if (asalCermin == "Cermin Cekung") {
            paint.textSize = 18f
            val wiKuning: String =
                String.format("R : %.1f cm", rLingkaran + 2f * y)
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

        } else {
            paint.textSize = 18f
            val wiKuning: String =
                String.format("R : %.1f cm", rLingkaran + 2f * y)
            val txtWidthKuning = paint.measureText(wiKuning)
            val txtHeightKuning = paint.descent() - paint.ascent()
            canvas.drawText(
                wiKuning,
                (width - txtWidthKuning) / 2f + 2f * y,
                height - txtHeightKuning,
                paint
            )

            val wiHijau: String =
                String.format("F : %.1f cm", titikFokus)
            val txtWidthHijau = paint.measureText(wiHijau)
            val txtHeightHijau = paint.descent() - paint.ascent()
            canvas.drawText(
                wiHijau,
                ((width - radiusna!!) - txtWidthHijau) / 2f + y,
                height - txtHeightHijau,
                paint
            )
        }

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

    fun switchCondition() {
        // Mulai animasi saat kondisi berubah
        animatorCermin.start()
        animatorLatarMerah.start()
    }

}
