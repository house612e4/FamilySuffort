package com.mtstudio.familysupport

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnEnable = findViewById<Button>(R.id.btnEnable)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        btnEnable.setOnClickListener {
            // Accessibility Settings এ নিয়ে যাবে
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        tvStatus.text = "Family Support সক্রিয় করতে নিচের বাটন চাপুন"
    }
}
