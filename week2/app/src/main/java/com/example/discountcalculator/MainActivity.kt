package com.example.discountcalculator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val ivProfile: ImageView = findViewById(R.id.ivProfile)
            ivProfile.setImageURI(it)
            imageUri = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        val etName: EditText = findViewById(R.id.etName)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val spinner: Spinner = findViewById(R.id.citySpinner)
        val btnRegister: Button = findViewById(R.id.btnSubmit)
        val rbMale: RadioButton = findViewById(R.id.rbMale)
        val cbTerm: CheckBox = findViewById(R.id.cbTerms)
        val btnSelectImage: Button = findViewById(R.id.btnSelectImage)

        btnSelectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        val countries = arrayOf("USA", "UK", "Pakistan", "India", "Canada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateName(s.toString())
            }
        })

        etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validatePassword(s.toString())
            }
        })

        btnRegister.setOnClickListener {
            if (validateInputs()) {
                val name = etName.text.toString()
                val selectedGender = if (rbMale.isChecked) "Male" else "Female"
                val selectedCountry = spinner.selectedItem.toString()

                Toast.makeText(this, "Registering $selectedGender from $selectedCountry", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, welcomeActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("gender", selectedGender)
                    putExtra("imageUri", imageUri.toString())
                }
                startActivity(intent)
            }
        }
    }

    private fun validateInputs(): Boolean {
        val nameValid = validateName(findViewById<EditText>(R.id.etName).text.toString())
        val passwordValid = validatePassword(findViewById<EditText>(R.id.etPassword).text.toString())
        val termsChecked = findViewById<CheckBox>(R.id.cbTerms).isChecked

        if (!termsChecked) {
            Toast.makeText(this, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
        }

        return nameValid && passwordValid && termsChecked
    }

    private fun validateName(name: String): Boolean {
        val etName: EditText = findViewById(R.id.etName)
        if (name.length <= 3 || !name.all { it.isLetter() }) {
            etName.error = "Invalid name. Must be > 3 characters and only contain letters."
            return false
        } else {
            etName.error = null
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        val etPassword: EditText = findViewById(R.id.etPassword)
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters long"
            return false
        } else {
            etPassword.error = null
        }
        return true
    }
}
