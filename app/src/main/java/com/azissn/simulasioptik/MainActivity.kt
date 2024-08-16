package com.azissn.simulasioptik

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azissn.simulasioptik.MyCanvas.MyCanvas
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {
    private lateinit var myCanvas: MyCanvas

    private lateinit var sliderX: Slider
    private lateinit var sliderY: Slider
    private lateinit var btnGanti: Button

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myCanvas = findViewById(R.id.myCanvas)
        sliderX = findViewById(R.id.sliderX)
        sliderY = findViewById(R.id.sliderY)
        btnGanti = findViewById(R.id.gantiCermin)

        sliderX.addOnChangeListener { _, value, _ ->
            myCanvas.setCoordinates(value, sliderY.value)
        }

        sliderY.addOnChangeListener { _, value, _ ->
            myCanvas.setCoordinates(sliderX.value, value)
        }

        btnGanti.setOnClickListener {
            myCanvas.gantiCermin()
            myCanvas.switchCondition()

        }

        hideSystemUI()
    }

    // Exit with klick back button 2 times
    @Suppress("DEPRECATI ON")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()

        // Reset to start condition after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    )
        }
    }
}
