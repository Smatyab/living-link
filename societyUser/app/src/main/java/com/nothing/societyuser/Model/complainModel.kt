package com.nothing.societyuser.Model

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class complainModel{
    var type: String = ""
    var title:String = ""
    var description:String = ""
    var imageUrl:String = ""
    var userHouseNo = "0000"
    var societyName =""
    var approved = false
    var rejected = false
    var resolved = false
    var timestamp: Timestamp = Timestamp.now()

    constructor(type: String, title: String, description: String, imageUrl: String,userHouseNo:String,societyName: String) {
        this.type = type
        this.title = title
        this.description = description
        this.imageUrl = imageUrl
        this.userHouseNo = userHouseNo
        this.societyName = societyName
    }
    constructor()

    fun upload(intent: Context) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        db.collection("member").document(user!!.uid).get().addOnSuccessListener {
            val societyId = it.get("societyId").toString()

            db.collection("societies").document(societyId).collection("complains").add(this)
                .addOnSuccessListener {
                    Toast.makeText(intent, "Complaint Raised!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(intent, "Failed to Create Complaint!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
