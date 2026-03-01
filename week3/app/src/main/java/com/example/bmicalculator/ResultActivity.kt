package com.example.bmicalculator

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val name = intent.getStringExtra("NAME")
        val bmi = intent.getDoubleExtra("BMI", 0.0)

        val tvGreeting = findViewById<TextView>(R.id.tv_greeting)
        val tvBmiResult = findViewById<TextView>(R.id.tv_bmi_result)
        val tvBmiCategory = findViewById<TextView>(R.id.tv_bmi_category)
        val cvResult = findViewById<CardView>(R.id.cv_result)

        tvGreeting.text = "Hello, $name!"
        tvBmiResult.text = String.format("%.1f", bmi)

        val (category, color) = getBmiCategory(bmi)
        tvBmiCategory.text = category
        cvResult.setCardBackgroundColor(color)
    }

    private fun getBmiCategory(bmi: Double): Pair<String, Int> {
        return when {
            bmi < 18.5 -> "Underweight" to Color.parseColor("#3498db") // Blue
            bmi < 25 -> "Normal" to Color.parseColor("#2ecc71")      // Green
            bmi < 30 -> "Overweight" to Color.parseColor("#f1c40f")   // Yellow
            else -> "Obese" to Color.parseColor("#e74c3c")         // Red
        }
    }
}