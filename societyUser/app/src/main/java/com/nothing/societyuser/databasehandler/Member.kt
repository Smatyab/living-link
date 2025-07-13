package com.nothing.societyuser.databasehandler

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class Member() {
    var transactions = ArrayList<Transaction>()
    init {
        val auth = Firebase.auth
        val user = auth.currentUser

        val fireStore = Firebase.firestore

        fireStore.collection("member").document(user?.uid ?: "C5r6cqwiemodtmaudeAm").collection("transactions").get()
            .addOnSuccessListener { result ->
//                this.transactions = document.toObject(Member::class.java)?.transactions ?: ArrayList()
                for (document in result) {
                    Log.d("Doc", document.id)
                    this.transactions.add(Transaction(
                        document.data["id"].toString(),
                        document.data["amount"].toString().toInt(),
                        document.data["completed"].toString().toBoolean()
                    ))
                }
                Log.d("Member", "Transactions: $transactions")
                Log.d("Member", "UID: ${user?.uid ?: "C5r6cqwiemodtmaudeAm"}")
            }

    }
}