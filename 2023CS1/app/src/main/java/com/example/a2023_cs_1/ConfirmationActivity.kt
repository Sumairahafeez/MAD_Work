package com.example.a2023_cs_1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        val tvName = findViewById<TextView>(R.id.tv_conf_name)
        val tvPhone = findViewById<TextView>(R.id.tv_conf_phone)
        val tvEmail = findViewById<TextView>(R.id.tv_conf_email)
        val tvType = findViewById<TextView>(R.id.tv_conf_type)
        val tvDate = findViewById<TextView>(R.id.tv_conf_date)
        val ivConfImage = findViewById<ImageView>(R.id.iv_conf_image)
        val btnBackHome = findViewById<Button>(R.id.btn_back_home)

        val name = intent.getStringExtra("FULL_NAME")
        val phone = intent.getStringExtra("PHONE")
        val email = intent.getStringExtra("EMAIL")
        val type = intent.getStringExtra("EVENT_TYPE")
        val date = intent.getStringExtra("EVENT_DATE")
        val imageUriString = intent.getStringExtra("IMAGE_URI")

        tvName.text = "Name: $name"
        tvPhone.text = "Phone: $phone"
        tvEmail.text = "Email: $email"
        tvType.text = "Event Type: $type"
        tvDate.text = "Date: $date"

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            ivConfImage.setImageURI(imageUri)
        }

        btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}