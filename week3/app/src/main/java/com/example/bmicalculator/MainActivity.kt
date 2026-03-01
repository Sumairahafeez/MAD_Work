package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etName = findViewById<EditText>(R.id.et_name)
        val etAge = findViewById<EditText>(R.id.et_age)
        val etHeight = findViewById<EditText>(R.id.et_height)
        val etWeight = findViewById<EditText>(R.id.et_weight)
        val btnCalculate = findViewById<Button>(R.id.btn_calculate)

        btnCalculate.setOnClickListener {
            val name = etName.text.toString()
            val age = etAge.text.toString().toInt()
            val height = etHeight.text.toString().toDouble()
            val weight = etWeight.text.toString().toDouble()

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("NAME", name)
            intent.putExtra("AGE", age)
            intent.putExtra("HEIGHT", height)
            intent.putExtra("WEIGHT", weight)
            startActivity(intent)
        }
    }
}