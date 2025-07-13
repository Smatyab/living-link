package com.nothing.secad

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Date

fun createComplain(societyId: String, description: String) {
    val complain = Complain(Date(), description, false, false)

    val fireStore = Firebase.firestore
    fireStore.collection("societies").document(societyId)
        .collection("complains").add(complain)
}

fun approveComplain(societyId: String, complainId: String) {
    val fireStore = Firebase.firestore
    fireStore.collection("societies").document(societyId)
        .collection("complains").document(complainId).update("approved", true)
}
