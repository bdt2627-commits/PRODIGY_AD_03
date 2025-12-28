package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnPause: Button
    private lateinit var btnReset: Button

    // Time tracking variables
    private var isRunning = false
    private var timeInMilliseconds: Long = 0L
    private var startTime: Long = 0L

    // Handler for updating the UI
    private val handler = Handler(Looper.getMainLooper())

    // Runnable to update the timer text
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                // Calculate elapsed time
                timeInMilliseconds = System.currentTimeMillis() - startTime
                updateTimerText()
                // Schedule the next update (e.g., every 10 milliseconds for smooth milliseconds display)
                handler.postDelayed(this, 10)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        textViewTimer = findViewById(R.id.text_view_timer)
        btnStart = findViewById(R.id.button_start)
        btnPause = findViewById(R.id.button_pause)
        btnReset = findViewById(R.id.button_reset)

        // 2. Initial Button State
        btnPause.isEnabled = false

        // 3. Set up Click Listeners
        btnStart.setOnClickListener { startTimer() }
        btnPause.setOnClickListener { pauseTimer() }
        btnReset.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            // Save the starting point, adjusted for any paused time
            startTime = System.currentTimeMillis() - timeInMilliseconds
            handler.post(runnable) // Start the update loop

            // Update button states
            btnStart.isEnabled = false
            btnPause.isEnabled = true
        }
    }

    private fun pauseTimer() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(runnable) // Stop the update loop

            // Update button states
            btnStart.isEnabled = true
            btnPause.isEnabled = false
        }
    }

    private fun resetTimer() {
        pauseTimer() // Stop the timer if running
        timeInMilliseconds = 0L
        updateTimerText() // Update the display to 00:00:000

        // Reset button states
        btnStart.isEnabled = true
        btnPause.isEnabled = false
    }

    private fun updateTimerText() {
        // Calculate Minutes, Seconds, and Milliseconds from total time
        val totalSeconds = timeInMilliseconds / 1000
        val minutes = (totalSeconds / 60) % 60
        val seconds = totalSeconds % 60
        (timeInMilliseconds % 1000) / 10 // Milliseconds are usually shown as 3 digits

        // Format the time as MM:SS:MMM
        val timeString = String.format(
            Locale.getDefault(),
            "%02d:%02d:%03d",
            minutes,
            seconds,
            timeInMilliseconds % 1000
        )
        textViewTimer.text = timeString
    }
}