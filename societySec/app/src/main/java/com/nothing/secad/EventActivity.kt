package com.nothing.secad

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.secad.databinding.ActivityEventBinding
import com.google.firebase.Timestamp


class EventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireBaseDataCall()
        binding.eventCallButton.setOnClickListener(){
            makeCall()
        }

        binding.eventMailButton.setOnClickListener(){
            var content ="Description :  "+ binding.eventDesc.text.toString()
            var title = "Title :  "+ binding.eventTitle.text.toString()+"\n\n Write More..."
            var email = "llEvent@test.com"
            sendEmail(email,content,title)
        }
    }

    fun fireBaseDataCall(){


// Get the document
        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

// Get a reference to the document
        val adminDocRef = db.collection("ADMIN").document("XqHXv5Ebd8aUXngVzSXq")
        val eventDocRef = adminDocRef.collection("events").document("JhXHevLoi5JUTsXJhoYH")

// Get the document
        eventDocRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // Accessing data inside the document
                    var dateField:String? = document.getString("eventDatetime")!!.take(10).toString() // Accessing the value of the "date" field
                    var expDate: String? = document.getString("bookingDate")!!.take(10)
                    var eventTitle:String? = document.getString("title").toString()
                    var eventDesc:String? = document.getString("eventDescription").toString()
                    var eventImage:String? = document.getString("eventImage").toString()


                    binding.eventDate.setText(dateField)
                    binding.eventExpDate.setText(expDate)
                    binding.eventTitle.setText("Title ${eventTitle}")
                    binding.eventDesc.setText("Description ${eventDesc}")


                    eventImage?.let {
                        Glide.with(this) // Use 'this' if you are inside an Activity or Fragment
                            .load(eventImage)
                            .into(binding.eventImage) // Replace 'imageView' with your ImageView's id
                    }
                    // Accessing nested fields
//                    val nestedField = document.get("nestedDocument.nestedField") as String?

                    // Print the values
                    println("Date: $dateField")
//                    println("Nested Field: $nestedField")
                } else {
                    println("Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting document: $e")
            }
    }


    private fun sendEmail(email: String,content: String, title:String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.setType("message/rfc822")
        startActivity(Intent.createChooser(intent, "Send issue..."))
    }

    private fun makeCall(){
        val phoneNumber = "0000000000"

        // Create intent with action ACTION_CALL
        val intent = Intent(Intent.ACTION_CALL)

        // Set the data for the intent (phone number to call)
        intent.data = Uri.parse("tel:$phoneNumber")

        // Check if the CALL_PHONE permission is granted before making the call
        if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Start the call
            startActivity(intent)
        } else {
            // Request CALL_PHONE permission if not granted
            requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 1)
        }
    }
}