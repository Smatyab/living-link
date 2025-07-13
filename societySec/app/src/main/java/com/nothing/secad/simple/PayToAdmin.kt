package com.nothing.secad.simple

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nothing.secad.Transaction
import com.nothing.secad.databinding.ActivityPayToAdminBinding
import java.util.Date

class PayToAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityPayToAdminBinding
    private var currentAmount:Long =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayToAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentAmountCheck()



        val transactionId = intent.getStringExtra("transactionId")
        val transactionAmount = intent.getIntExtra("transactionAmount", 0)
        val transactionAmountDouble: Double = transactionAmount.toDouble()
        binding.edtPayToAdmin.setText(transactionAmount.toString())

        binding.btnPayToLl.setOnClickListener {
            if (transactionId != null) {
                createTransaction(
                    transactionAmount,
                    transactionId,
                    Intent(this, SendPaymentAdmin::class.java),
                    Intent(this, SendPaymentAdmin::class.java)
                )
            } else {
                Toast.makeText(this, "Failed to Load Transaction Id", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun createTransaction(
        amount: Int,
        transactionId: String,
        successIntent: Intent,
        failureIntent: Intent
    ) {
        // Create a new transaction
        val transaction = Transaction(Timestamp(Date()), amount)

        // Add the transaction to the database
        val db = Firebase.firestore
        db.collection("societies")
            .document(Firebase.auth.currentUser?.uid ?: "").collection("transactions")
            .document(transactionId)
            .update("completed", true)
            // Success
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!")
                startActivity(successIntent)
            }
            // Failure
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error writing document", e)
                Toast.makeText(this@PayToAdmin, e.toString(), Toast.LENGTH_LONG).show()
                Log.e(ContentValues.TAG, e.toString() + ":" + Firebase.auth.currentUser!!.uid)

                val signUpIntent = Intent(this, SignupActivity::class.java)
                startActivity(failureIntent)
            }

        db.collection("ADMIN").document("XqHXv5Ebd8aUXngVzSXq").get().addOnSuccessListener {

            val balance = it.get("balance") as Long
            db.collection("ADMIN").document("XqHXv5Ebd8aUXngVzSXq")
                .update("balance", balance + amount)
        }

        db.collection("societies").document(Firebase.auth.currentUser?.uid ?: "").get()
            .addOnSuccessListener {
                val balance = it.get("currentAmount") as Long
                db.collection("societies").document(Firebase.auth.currentUser?.uid ?: "")
                    .update("currentAmount", balance - amount)
            }
    }

    fun currentAmountCheck(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection("societies").document(uid!!)

        userDocRef.get().addOnSuccessListener { documentSnapshot ->
            currentAmount = documentSnapshot.getLong("currentAmount") ?: 0L
            Toast.makeText(this, currentAmount.toString(), Toast.LENGTH_SHORT).show()
            // Use the currentAmount value as needed
            // ...
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}
