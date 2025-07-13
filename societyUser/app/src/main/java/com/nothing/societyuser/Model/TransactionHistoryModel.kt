package com.nothing.societyuser.Model

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

data class TransactionHistoryModel(
    var date: Date,
    var amount: Int,
    var status: Boolean,
    val id: String
)

public fun createTransactionHistoryModel(date: Date, amount: Int, status: Boolean, id: String): TransactionHistoryModel {
    return TransactionHistoryModel(date, amount, status, id)
}

public fun payTransaction(transactionId: String, intent: Context) {
//    var newTransactionHistoryModel = transactionHistoryModel.copy(status = true)

    // upload transaction to firebase

    val fireStore = Firebase.firestore
    val user = Firebase.auth.currentUser

    fireStore.collection("member").document(user?.uid ?: "C5r6cqwiemodtmaudeAm")
        .collection("transactions").document(transactionId).update(
            "completed", true
        )
        .addOnSuccessListener {
            Toast.makeText(intent, "Transaction Success", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(intent, "Transaction Failed", Toast.LENGTH_SHORT).show()
        }
}