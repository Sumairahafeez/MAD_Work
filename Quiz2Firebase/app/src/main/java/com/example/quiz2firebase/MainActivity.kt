package com.example.quiz2firebase

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        val testButton = findViewById<Button>(R.id.testButton)

        testButton.setOnClickListener {
            testFirebaseWrite(statusTextView)
        }
    }

    private fun testFirebaseWrite(statusTextView: TextView) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("connection_test")

        statusTextView.text = "Writing to Firebase..."
        
        myRef.setValue("Success at ${System.currentTimeMillis()}")
            .addOnSuccessListener {
                statusTextView.text = "Firebase Write Successful!"
            }
            .addOnFailureListener { e ->
                statusTextView.text = "Firebase Write Failed: ${e.message}"
            }
    }
}
