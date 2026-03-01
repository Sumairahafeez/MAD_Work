package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tilName = findViewById<TextInputLayout>(R.id.til_name)
        val tilAge = findViewById<TextInputLayout>(R.id.til_age)
        val tilHeight = findViewById<TextInputLayout>(R.id.til_height)
        val tilWeight = findViewById<TextInputLayout>(R.id.til_weight)
        val btnCalculate = findViewById<Button>(R.id.btn_calculate)

        btnCalculate.setOnClickListener {
            val name = tilName.editText?.text.toString()
            val age = tilAge.editText?.text.toString()
            val heightStr = tilHeight.editText?.text.toString()
            val weightStr = tilWeight.editText?.text.toString()

            if (validateInput(name, age, heightStr, weightStr)) {
                val height = heightStr.toDouble()
                val weight = weightStr.toDouble()

                val bmi = weight / ((height / 100) * (height / 100))

                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("NAME", name)
                intent.putExtra("BMI", bmi)
                startActivity(intent)
            }
        }
    }

    private fun validateInput(name: String, age: String, height: String, weight: String): Boolean {
        val tilName = findViewById<TextInputLayout>(R.id.til_name)
        val tilAge = findViewById<TextInputLayout>(R.id.til_age)
        val tilHeight = findViewById<TextInputLayout>(R.id.til_height)
        val tilWeight = findViewById<TextInputLayout>(R.id.til_weight)

        tilName.error = if (name.isBlank()) "Please enter your name" else null
        tilAge.error = if (age.isBlank()) "Please enter your age" else null
        tilHeight.error = if (height.isBlank()) "Please enter your height" else null
        tilWeight.error = if (weight.isBlank()) "Please enter your weight" else null

        return name.isNotBlank() && age.isNotBlank() && height.isNotBlank() && weight.isNotBlank()
    }
}