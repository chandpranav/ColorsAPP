package com.example.colorsapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.colorsapp.viewmodel.MyViewModel
import com.example.colorsapp.data.MyDataStoreRepository

const val Log_Tag = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val myViewModel: MyViewModel by lazy {
        MyDataStoreRepository.initialize(this)
        ViewModelProvider(this)[MyViewModel::class.java]
    }

    lateinit var switchRed: Switch
    lateinit var switchGreen: Switch
    lateinit var switchBlue: Switch
    lateinit var colorDisplay: TextView
    lateinit var valueRed: EditText
    lateinit var valueGreen: EditText
    lateinit var valueBlue: EditText
    lateinit var resetBtn: Button
    lateinit var redSeek: SeekBar
    lateinit var greenSeek: SeekBar
    lateinit var blueSeek: SeekBar
    private var lastProcessRed: Int = 0
    private var lastProcessGreen: Int = 0
    private var lastProcessBlue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectViews()
        callbacks()
        loadData()
    }

    private fun connectViews() {
        switchRed = findViewById(R.id.switch1)
        switchGreen = findViewById(R.id.switch2)
        switchBlue = findViewById(R.id.switch3)
        colorDisplay = findViewById(R.id.textView)
        valueRed = findViewById(R.id.editTextText1)
        valueGreen = findViewById(R.id.editTextText2)
        valueBlue = findViewById(R.id.editTextText3)
        resetBtn = findViewById(R.id.reset)
        redSeek = findViewById(R.id.seekBar1)
        greenSeek = findViewById(R.id.seekBar2)
        blueSeek = findViewById(R.id.seekBar3)
    }

    private fun callbacks() {
        redSeek.setOnSeekBarChangeListener(seekbarListener(valueRed, 1) { redSeek.progress })
        greenSeek.setOnSeekBarChangeListener(seekbarListener(valueGreen, 2) { greenSeek.progress })
        blueSeek.setOnSeekBarChangeListener(seekbarListener(valueBlue, 3) { blueSeek.progress })

        editListener(valueRed, redSeek)
        editListener(valueGreen, greenSeek)
        editListener(valueBlue, blueSeek)

        resetBtn.setOnClickListener {
            val default = 128
            switchRed.isChecked = true
            switchGreen.isChecked = true
            switchBlue.isChecked = true
            redSeek.progress = default
            greenSeek.progress = default
            blueSeek.progress = default
        }

        switchRed.setOnCheckedChangeListener { _, isChecked -> toggleColor(isChecked, valueRed, redSeek, 4, ::lastProcessRed) }
        switchGreen.setOnCheckedChangeListener { _, isChecked -> toggleColor(isChecked, valueGreen, greenSeek, 5, ::lastProcessGreen) }
        switchBlue.setOnCheckedChangeListener { _, isChecked -> toggleColor(isChecked, valueBlue, blueSeek, 6, ::lastProcessBlue) }
    }

    private fun seekbarListener(editText: EditText, index: Int, progressProvider: () -> Int) =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                editText.setText("%.3f".format(progress / 255f))
                setRGBcolor()
            }

            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {
                myViewModel.saveInput(progressProvider(), index)
            }
        }

    private fun editListener(editText: EditText, seekBar: SeekBar) {
        editText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                try {
                    val floatVal = editText.text.toString().toFloat()
                    val progress = (floatVal * 255).toInt()
                    if (progress in 0..255) {
                        seekBar.progress = progress
                    } else {
                        Toast.makeText(this, "Enter value between 0 and 1", Toast.LENGTH_SHORT).show()
                    }
                } catch (_: NumberFormatException) {}
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun toggleColor(isChecked: Boolean, editText: EditText, seekBar: SeekBar, index: Int, lastValueRef: () -> Int) {
        editText.isEnabled = isChecked
        seekBar.isEnabled = isChecked
        if (isChecked) {
            if (lastValueRef() != 0) seekBar.progress = lastValueRef()
        } else {
            lastValueRef().let { seekBar.progress = 0 }
        }
        myViewModel.saveSwitchInput(isChecked, index)
    }

    private fun loadData() {
        myViewModel.loadInputs(this)
    }

    fun setRGBcolor() {
        val hex = String.format("#%02x%02x%02x", redSeek.progress, greenSeek.progress, blueSeek.progress)
        colorDisplay.setBackgroundColor(Color.parseColor(hex))
    }
}