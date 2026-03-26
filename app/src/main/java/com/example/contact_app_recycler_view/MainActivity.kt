package com.example.contact_app_recycler_view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity(), ContactAdapter.OnContactActionListener {
    private lateinit var etName: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etSearch: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnSort: MaterialButton
    private lateinit var btnToggleLayout: MaterialButton
    private lateinit var ivNewContactImage: ImageView
    private lateinit var recyclerViewContacts: RecyclerView

    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Contact>()
    private var selectedImageUri: Uri? = null
    private var editImageUri: Uri? = null
    private var ivEditContactImageInDialog: ImageView? = null
    private var isGridView = true

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            selectedImageUri = it
            ivNewContactImage.setImageURI(it)
        }
    }

    private val pickEditImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            editImageUri = it
            ivEditContactImageInDialog?.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize UI components
        etName = findViewById(R.id.etName)
        etPhone = findViewById(R.id.etPhone)
        etSearch = findViewById(R.id.etSearch)
        btnSave = findViewById(R.id.btnSave)
        btnSort = findViewById(R.id.btnSort)
        btnToggleLayout = findViewById(R.id.btnToggleLayout)
        ivNewContactImage = findViewById(R.id.ivNewContactImage)
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView
        contactAdapter = ContactAdapter(contactList, this)
        updateLayoutManager()
        recyclerViewContacts.adapter = contactAdapter

        ivNewContactImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            saveContact()
        }

        btnSort.setOnClickListener {
            sortContacts()
        }

        btnToggleLayout.setOnClickListener {
            isGridView = !isGridView
            contactAdapter.isGridView = isGridView
            updateLayoutManager()
            // Re-bind adapter to apply new layout resource in onCreateViewHolder
            recyclerViewContacts.adapter = contactAdapter 
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                contactAdapter.filter(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        findViewById<Button>(R.id.btnLoadContacts).setOnClickListener {
            checkPermissionAndLoadContacts()
        }
    }

    private fun updateLayoutManager() {
        if (isGridView) {
            recyclerViewContacts.layoutManager = GridLayoutManager(this, 2)
            btnToggleLayout.setIconResource(android.R.drawable.ic_menu_agenda) // List icon
        } else {
            recyclerViewContacts.layoutManager = LinearLayoutManager(this)
            btnToggleLayout.setIconResource(android.R.drawable.ic_menu_view) // Grid icon
        }
    }

    private fun sortContacts() {
        contactList.sortBy { it.name.lowercase(Locale.getDefault()) }
        contactAdapter.updateList(contactList)
        Toast.makeText(this, "Sorted A-Z", Toast.LENGTH_SHORT).show()
    }

    private fun saveContact() {
        val name = etName.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (!validateInputs(name, phone, etName, etPhone)) {
            return
        }

        val newContact = Contact(name, phone, selectedImageUri?.toString())
        contactList.add(newContact)
        contactAdapter.updateList(contactList)
        recyclerViewContacts.scrollToPosition(contactList.size - 1)

        Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show()

        etName.text?.clear()
        etPhone.text?.clear()
        ivNewContactImage.setImageResource(android.R.drawable.ic_menu_gallery)
        selectedImageUri = null
        etName.requestFocus()
    }

    private fun validateInputs(
        name: String,
        phone: String,
        nameInput: TextInputEditText,
        phoneInput: TextInputEditText
    ): Boolean {
        var isValid = true
        if (name.isEmpty()) {
            nameInput.error = "Name is required"
            isValid = false
        }
        if (phone.isEmpty()) {
            phoneInput.error = "Phone number is required"
            isValid = false
        } else if (phone.length < 10 || !phone.all { it.isDigit() || it == '+' }) {
            phoneInput.error = "Enter valid phone number"
            isValid = false
        }
        return isValid
    }

    override fun onItemClick(position: Int) {
        // Implementation for item click
    }

    override fun onEditClick(position: Int, contact: Contact) {
        showEditDialog(contact)
    }

    override fun onDeleteClick(position: Int, contact: Contact) {
        showDeleteDialog(contact)
    }

    private fun showDeleteDialog(contact: Contact) {
        AlertDialog.Builder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Yes") { _, _ ->
                contactList.remove(contact)
                contactAdapter.updateList(contactList)
                Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showEditDialog(contact: Contact) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_dialog_edit_item, null)
        val etEditName = dialogView.findViewById<TextInputEditText>(R.id.etEditName)
        val etEditPhone = dialogView.findViewById<TextInputEditText>(R.id.etEditPhone)
        ivEditContactImageInDialog = dialogView.findViewById(R.id.ivEditContactImage)

        etEditName.setText(contact.name)
        etEditPhone.setText(contact.phone)
        editImageUri = contact.imageUri?.let { Uri.parse(it) }
        
        if (editImageUri != null) {
            ivEditContactImageInDialog?.setImageURI(editImageUri)
        } else {
            ivEditContactImageInDialog?.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        ivEditContactImageInDialog?.setOnClickListener {
            pickEditImageLauncher.launch("image/*")
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Contact")
            .setView(dialogView)
            .setPositiveButton("Update", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val updatedName = etEditName.text.toString().trim()
            val updatedPhone = etEditPhone.text.toString().trim()

            if (validateInputs(updatedName, updatedPhone, etEditName, etEditPhone)) {
                contact.name = updatedName
                contact.phone = updatedPhone
                contact.imageUri = editImageUri?.toString()
                contactAdapter.updateList(contactList)
                Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    private fun checkPermissionAndLoadContacts() {
        val readContactsPermission = Manifest.permission.READ_CONTACTS
        if (ContextCompat.checkSelfPermission(this, readContactsPermission) == PackageManager.PERMISSION_GRANTED) {
            loadContactsFromPhone()
        } else {
            requestPermissionLauncher.launch(readContactsPermission)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            loadContactsFromPhone()
        } else {
            Toast.makeText(this, "Contacts permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadContactsFromPhone() {
        val loadedContacts = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val phone = it.getString(phoneIndex) ?: ""
                val photoUri = it.getString(photoIndex)

                if (name.isNotBlank() && phone.isNotBlank()) {
                    loadedContacts.add(Contact(name, phone, photoUri))
                }
            }
        }

        contactList.addAll(loadedContacts)
        contactAdapter.updateList(contactList)

        if (loadedContacts.isNotEmpty()) {
            Toast.makeText(this, "${loadedContacts.size} contacts loaded", Toast.LENGTH_SHORT).show()
        }
    }
}
