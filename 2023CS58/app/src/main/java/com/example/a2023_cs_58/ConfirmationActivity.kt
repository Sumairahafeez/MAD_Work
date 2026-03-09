package com.example.a2023_cs_58

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_confirmation2)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find TextView
        val tvSummary = findViewById<TextView>(R.id.tvSummary)

        // Get data from Intent
        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val type = intent.getStringExtra("type")
        val gender = intent.getStringExtra("gender")

        // Display the summary
        tvSummary.text = """
            Appointment Confirmed
            
            Name: $name
            Phone: $phone
            Email: $email
            Appointment Type: $type
            Gender: $gender
        """.trimIndent()
    }
}