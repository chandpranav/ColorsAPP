package com.example.colorsapp

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var colorView: View
    private lateinit var seekRed: SeekBar
    private lateinit var seekGreen: SeekBar
    private lateinit var seekBlue: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorView = findViewById(R.id.colorView)
        seekRed = findViewById(R.id.seekRed)
        seekGreen = findViewById(R.id.seekGreen)
        seekBlue = findViewById(R.id.seekBlue)

        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        }

        seekRed.setOnSeekBarChangeListener(listener)
        seekGreen.setOnSeekBarChangeListener(listener)
        seekBlue.setOnSeekBarChangeListener(listener)
    }

    private fun updateColor() {
        val color = Color.rgb(seekRed.progress, seekGreen.progress, seekBlue.progress)
        colorView.setBackgroundColor(color)
    }
}
