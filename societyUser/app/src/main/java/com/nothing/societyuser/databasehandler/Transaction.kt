package com.nothing.societyuser.databasehandler

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date

class Transaction(userId: String,  amountOfTran: Number, some: Boolean = true) {
    var id: String = Date().time.toString()
    var amount: Number = amountOfTran
    var date: Date = Date()
    var completed: Boolean = false

    init {
        if (some) {
            val fireStore = Firebase.firestore
            fireStore.collection("transactions").document(userId).update(id, this)
        }
    }

}