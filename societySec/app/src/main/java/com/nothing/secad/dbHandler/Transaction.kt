package com.nothing.secad.dbHandler

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date


class Transaction(userId: String,  amountOfTran: Number) {
    var id: String = Date().time.toString()
    var amount: Number = amountOfTran
    var date: Date = Date()
    var completed: Boolean = false

    init {
        val fireStore = Firebase.firestore
        fireStore.collection("member").document(userId).collection("transactions").add(hashMapOf(
            "id" to id,
            "amount" to amount,
            "date" to date,
            "completed" to completed,
        ))
    }
}

