package com.nothing.societyuser.welcome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.R
import com.nothing.societyuser.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseDataCall()
    }

        fun fireBaseDataCall() {


            // Get the document
            // Get Firestore instance
            val db = FirebaseFirestore.getInstance()

            // Get a reference to the document
            val adminDocRef = db.collection("ADMIN").document("XqHXv5Ebd8aUXngVzSXq")
            val eventDocRef = adminDocRef.collection("events").document("JhXHevLoi5JUTsXJhoYH")

            // Get the document
            eventDocRef.get()
                .addOnSuccessListener { document ->
                    var dateField: String? =
                        document.getString("eventDatetime")?.take(10).toString()

                    var expDate: String? = document.getString("bookingDate")?.take(10)
                    var eventTitle: String? = document.getString("title")
                    var eventDesc: String? = document.getString("eventDescription")
                    var eventImage: String? = document.getString("eventImage")

// Check for null before accessing properties or calling methods
                    if (dateField != null) {
                        binding.eventDate.setText(dateField)
                    }
                    if (expDate != null) {
                        binding.eventExpDate.setText(expDate)
                    }
                    if (eventTitle != null) {
                        binding.eventTitle.setText("Title $eventTitle")
                    }
                    if (eventDesc != null) {
                        binding.eventDesc.setText("Description $eventDesc")
                    }
                    if (eventImage != null) {
                        Glide.with(this)
                            .load(eventImage)
                            .into(binding.eventImage)
                    }

                    // Accessing nested fields
                    //                    val nestedField = document.get("nestedDocument.nestedField") as String?

                    // Print the values
                    println("Date: $dateField")
                    //                    println("Nested Field: $nestedField")
                }
        }

}