package com.example.bmicalculator

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val name = intent.getStringExtra("NAME")
        val age = intent.getIntExtra("AGE", 0)
        val height = intent.getDoubleExtra("HEIGHT", 0.0)
        val weight = intent.getDoubleExtra("WEIGHT", 0.0)

        val bmi = weight / ((height / 100) * (height / 100))

        val tvName = findViewById<TextView>(R.id.tv_name)
        val tvAge = findViewById<TextView>(R.id.tv_age)
        val tvBmi = findViewById<TextView>(R.id.tv_bmi)

        tvName.text = "Name: $name"
        tvAge.text = "Age: $age"
        tvBmi.text = String.format("BMI: %.2f", bmi)
    }
}