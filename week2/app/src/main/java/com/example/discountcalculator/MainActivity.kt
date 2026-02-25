package com.example.discountcalculator

import android.os.Bundle
import android.widget.* // This imports Button, Spinner, Toast, etc.
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure this matches your XML file name!
        setContentView(R.layout.register_activity)

        // FIX 1: Remove the empty <> brackets or put the type inside them
        val spinner: Spinner = findViewById(R.id.citySpinner)
        val btnRegister: Button = findViewById(R.id.btnSubmit)
        val rbMale: RadioButton = findViewById(R.id.rbMale)
        val cbTerm: CheckBox = findViewById(R.id.cbTerms)
        // Setup Spinner Data
        val countries = arrayOf("USA", "UK", "Pakistan", "India", "Canada")

        // FIX 2: Added 'this' and the specific layout for the adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Button Click Logic
        btnRegister.setOnClickListener {
            if (cbTerm.isChecked) {
                val selectedGender = if (rbMale.isChecked) "Male" else "Female"
                val selectedCountry = spinner.selectedItem.toString()

                Toast.makeText(this, "Registering $selectedGender from $selectedCountry", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please accept terms", Toast.LENGTH_SHORT).show()
            }
        }
    }
}