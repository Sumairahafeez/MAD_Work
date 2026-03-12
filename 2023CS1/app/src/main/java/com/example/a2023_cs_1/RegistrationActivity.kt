package com.example.a2023_cs_1

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var spinnerEventType: Spinner
    private lateinit var tvSelectedDate: TextView
    private lateinit var rgGender: RadioGroup
    private lateinit var cbTerms: CheckBox
    private lateinit var ivProfileImage: ImageView
    private var selectedDate: String = ""
    private var imageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            ivProfileImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etFullName = findViewById(R.id.et_full_name)
        etPhone = findViewById(R.id.et_phone)
        etEmail = findViewById(R.id.et_email)
        spinnerEventType = findViewById(R.id.spinner_event_type)
        tvSelectedDate = findViewById(R.id.tv_selected_date)
        rgGender = findViewById(R.id.rg_gender)
        cbTerms = findViewById(R.id.cb_terms)
        ivProfileImage = findViewById(R.id.iv_profile_image)
        val btnPickDate = findViewById<Button>(R.id.btn_pick_date)
        val btnSubmit = findViewById<Button>(R.id.btn_submit)
        val btnUploadImage = findViewById<Button>(R.id.btn_upload_image)

        // Spinner Setup
        val eventTypes = arrayOf("Seminar", "Workshop", "Conference", "Webinar", "Cultural Event")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, eventTypes)
        spinnerEventType.adapter = adapter

        // Date Picker
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, R.style.Theme__2023CS1, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate.text = "Selected Date: $selectedDate"
            }, year, month, day)
            datePickerDialog.show()
        }

        // Image Upload
        btnUploadImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSubmit.setOnClickListener {
            if (validateFields()) {
                showCustomConfirmationDialog()
            }
        }
    }

    private fun validateFields(): Boolean {
        if (etFullName.text.isNullOrBlank()) {
            etFullName.error = "Full Name is required"
            return false
        }
        if (etPhone.text.isNullOrBlank() || etPhone.text!!.length < 10) {
            etPhone.error = "Enter a valid phone number"
            return false
        }
        if (etEmail.text.isNullOrBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text!!).matches()) {
            etEmail.error = "Enter a valid email"
            return false
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select an event date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Accept Terms and Conditions", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showCustomConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_layout, null)
        val dialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .setView(dialogView)
            .create()

        dialogView.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<MaterialButton>(R.id.btn_confirm).setOnClickListener {
            dialog.dismiss()
            navigateToConfirmation()
        }

        dialog.show()
    }

    private fun navigateToConfirmation() {
        val selectedGender = findViewById<RadioButton>(rgGender.checkedRadioButtonId).text.toString()
        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra("FULL_NAME", etFullName.text.toString())
            putExtra("PHONE", etPhone.text.toString())
            putExtra("EMAIL", etEmail.text.toString())
            putExtra("EVENT_TYPE", spinnerEventType.selectedItem.toString())
            putExtra("EVENT_DATE", selectedDate)
            putExtra("GENDER", selectedGender)
            putExtra("IMAGE_URI", imageUri.toString())
        }
        startActivity(intent)
    }
}
