package com.example.week4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class quizAcitivity : AppCompatActivity() {
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_acitivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Show username dialog before the quiz starts
        showUserNameDialog()

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val rgQuestion1 = findViewById<RadioGroup>(R.id.rgQuestion1)
        val rgQuestion2 = findViewById<RadioGroup>(R.id.rgQuestion2)
        val rgQuestion3 = findViewById<RadioGroup>(R.id.rgQuestion3)

        btnSubmit.setOnClickListener {
            if (rgQuestion1.checkedRadioButtonId == -1 || rgQuestion2.checkedRadioButtonId == -1 || rgQuestion3.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Finish Quiz?")
                    .setMessage("Ready to see your results? You cannot change your answers after submitting.")
                    .setPositiveButton("SUBMIT") { _, _ ->
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

                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("SCORE", score)
                        intent.putExtra("TOTAL", 3)
                        intent.putExtra("USER_NAME", userName)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("CANCEL", null)
                    .show()
            }
        }
    }

    private fun showUserNameDialog() {
        val input = EditText(this)
        input.hint = "Your Name"
        
        // Add padding to the EditText inside the dialog
        val container = FrameLayout(this)
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(60, 20, 60, 0)
        input.layoutParams = params
        container.addView(input)

        MaterialAlertDialogBuilder(this)
            .setTitle("Enter Your Name")
            .setMessage("Welcome to the quiz! Please introduce yourself to continue.")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("START") { _, _ ->
                userName = input.text.toString().trim()
                if (userName.isEmpty()) {
                    userName = "Guest Player"
                }
                Toast.makeText(this, "Good luck, $userName!", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}