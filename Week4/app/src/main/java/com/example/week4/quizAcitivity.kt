package com.example.week4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class quizAcitivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_acitivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val rgQuestion1 = findViewById<RadioGroup>(R.id.rgQuestion1)
        val rgQuestion2 = findViewById<RadioGroup>(R.id.rgQuestion2)
        val rgQuestion3 = findViewById<RadioGroup>(R.id.rgQuestion3)

        btnSubmit.setOnClickListener {
            var score = 0
            
            // Check Q1: Paris (q1Option2)
            if (rgQuestion1.checkedRadioButtonId == R.id.q1Option2) {
                score++
            }
            // Check Q2: Mars (q2Option2)
            if (rgQuestion2.checkedRadioButtonId == R.id.q2Option2) {
                score++
            }
            // Check Q3: 12 (q3Option2)
            if (rgQuestion3.checkedRadioButtonId == R.id.q3Option2) {
                score++
            }

            if (rgQuestion1.checkedRadioButtonId == -1 || rgQuestion2.checkedRadioButtonId == -1 || rgQuestion3.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("SCORE", score)
                intent.putExtra("TOTAL", 3)
                startActivity(intent)
                finish()
            }
        }
    }
}