package com.example.a2023_cs_58

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class BookAppointmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        // Step 1: Initialize UI components
        val name = findViewById<EditText>(R.id.etName)
        val phone = findViewById<EditText>(R.id.etPhone)
        val email = findViewById<EditText>(R.id.etEmail)
        val spinner = findViewById<Spinner>(R.id.spinnerType)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val terms = findViewById<CheckBox>(R.id.checkTerms)
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)

        // Step 2: Fill the spinner with appointment types
        val types = arrayOf(
            "Doctor Consultation",
            "Dentist Appointment",
            "Eye Specialist",
            "Skin Specialist",
            "General Checkup"
        )

        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, types)
        spinner.adapter = adapter

        // Step 3: Confirm button click listener
        btnConfirm.setOnClickListener {
            val nameText = name.text.toString()
            val phoneText = phone.text.toString()
            val emailText = email.text.toString()
            val typeText = spinner.selectedItem.toString()

            val selectedGenderId = radioGroup.checkedRadioButtonId
            val genderText = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            // Step 4: Validation
            if (nameText.isEmpty()) {
                name.error = "Enter Name"
                return@setOnClickListener
            }

            if (phoneText.isEmpty()) {
                phone.error = "Enter Phone"
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {
                email.error = "Enter Email"
                return@setOnClickListener
            }

            if (genderText.isEmpty()) {
                Toast.makeText(this, "Select Gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!terms.isChecked) {
                Toast.makeText(this, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 5: Send data to confirmation screen
            val intent = Intent(this, ConfirmationActivity::class.java)
            intent.putExtra("name", nameText)
            intent.putExtra("phone", phoneText)
            intent.putExtra("email", emailText)
            intent.putExtra("type", typeText)
            intent.putExtra("gender", genderText)

            startActivity(intent)
        }
    }
}
