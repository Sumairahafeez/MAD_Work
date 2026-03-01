package com.example.discountcalculator

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class welcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val username = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val imageUriString = intent.getStringExtra("imageUri")

        val welcomeText: TextView = findViewById(R.id.textView)
        welcomeText.text = "Welcome, $username ($gender)"

        if (imageUriString != null && imageUriString != "null") {
            val imageUri = Uri.parse(imageUriString)
            val profileImage: ImageView = findViewById(R.id.ivProfileWelcome)
            profileImage.setImageURI(imageUri)
        }
    }
}
