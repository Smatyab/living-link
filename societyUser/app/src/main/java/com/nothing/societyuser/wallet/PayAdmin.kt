package com.nothing.societyuser.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nothing.societyuser.Model.payTransaction
import com.nothing.societyuser.animation_transaction_succes
import com.nothing.societyuser.databinding.ActivityPayAdminBinding

class PayAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityPayAdminBinding
    private var currentAmount: Double = 0.0
    private var transactionProcessed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactionId = intent.getStringExtra("transactionId")
        val transactionAmount = intent.getIntExtra("transactionAmount", 0)
        val transactionAmountDouble: Double = transactionAmount.toDouble()
        binding.payAdminEdt.setText(transactionAmount.toString())

        binding.payAdminBtn.setOnClickListener {
            if (!transactionProcessed) {
                fetchCurrentAmount(transactionAmountDouble)
                val intent = Intent(this, animation_transaction_succes::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Transaction already processed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCurrentAmount(transactionAmount: Double) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val uid = user.uid
            val memberDocRef = firestore.collection("member").document(uid)

            // Fetch currentAmount without snapshot listener
            memberDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        currentAmount = documentSnapshot.getDouble("currentAmount") ?: 0.0

                        // Handle the fetched currentAmount value
                        Toast.makeText(this@PayAdmin, "Transaction Amount: $transactionAmount", Toast.LENGTH_SHORT)
                            .show()
                        Toast.makeText(this@PayAdmin, "Current Amount: $currentAmount", Toast.LENGTH_LONG).show()

                        if (currentAmount >= transactionAmount) {
                            currentAmount -= transactionAmount

                            // Update the currentAmount in Firestore
                            memberDocRef.update("currentAmount", currentAmount)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this@PayAdmin,
                                        "Transaction successful. Updated current amount in Firestore.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    handleCurrentAmount(currentAmount) // Handle the currentAmount value
                                    transactionProcessed = true // Set the flag to true after processing the transaction

                                    var socId = documentSnapshot.getString("societyId")
                                    firestore.collection("societies").document(socId!!).get().addOnSuccessListener {
                                        var amount = it.getDouble("currentAmount")

                                        if (amount != null) {
                                            amount += transactionAmount
                                        }
                                        else {
                                            amount = transactionAmount
                                        }
                                        firestore.collection("societies").document(socId).update("currentAmount", amount)
                                    }

                                    val transactionId: String? = intent.getStringExtra("transactionId")
                                    payTransaction(transactionId!!, this)
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(
                                        this@PayAdmin,
                                        "Error updating current amount in Firestore: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Toast.makeText(this@PayAdmin, "Insufficient balance", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@PayAdmin, "Document does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this@PayAdmin, "Error fetching current amount: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun handleCurrentAmount(currentAmount: Double) {
        // Handle the fetched currentAmount value
        // You can update UI elements, perform calculations, or any other operations with the currentAmount value
        // For example, you can display the currentAmount in a TextView
        Log.d("PayAdmin", "Handled Current Amount: $currentAmount")
    }
}
