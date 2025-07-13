package com.nothing.secad.simple

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nothing.secad.R
import com.nothing.secad.UserTransactionShowActivity
import com.nothing.secad.databinding.ActivityReceivePaymentUserBinding
import com.nothing.secad.dbHandler.Transaction
import com.nothing.secad.model.userTransaction

class ReceivePaymentUser : AppCompatActivity() {

    private lateinit var binding: ActivityReceivePaymentUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceivePaymentUserBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.userTransactionShowBtn.setOnClickListener(){
            var intent = Intent(this, UserTransactionShowActivity::class.java)
            startActivity(intent)

        }


        binding.maintenanceSendNotificationBtn.setOnClickListener(){


            val builder = AlertDialog.Builder(this)

            // Set the dialog properties
            builder.setTitle("maintenance")
            builder.setMessage("Send Request For Maintenance, Are You sure!!!")

            // Set the positive button
            builder.setPositiveButton("Yes") { dialog, which ->
                // Handle the positive button click
                sendNotification()
                Toast.makeText(this, "send successfully", Toast.LENGTH_SHORT).show()
            }

            // Set the negative button
            builder.setNegativeButton("No") { dialog, which ->
                // Handle the negative button click
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            }

            // Create and show the dialog
            val dialog = builder.create()
            dialog.show()

        }

    }

    fun sendNotification(){
        val db = Firebase.firestore
        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

        val documentReference = db.collection("societies").document(currentUser)
        documentReference.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Access the "members" field as an ArrayList
                    val membersList = document.get("memberIDs") as ArrayList<*>?

                    Log.d(TAG,  "Members: $membersList")

                    // Check if the field is not null before using it
                    if (membersList != null) {
                        // Print all member names

                        for (member in membersList) {
                            var amountMain = binding.amountMaintenanceEdt.text.toString()
                            var amountMainInt = amountMain.toInt()
                            Transaction("$member",amountMainInt)
                        }
                    } else {
                        println("Members field is null")
                    }
                } else {
                    println("Document not found")
                }
            }
    }

}