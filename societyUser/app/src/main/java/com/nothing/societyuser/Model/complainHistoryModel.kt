package com.nothing.societyuser.Model

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.Date

data class complainHistoryModel(
    var imgUrl: String,
    var type: String,
    var title: String,
    var date: Date,
    var status: String,
    var description: String,
    val userHouse: String,
)

public fun createComplainHistory(img:String,type:String,title:String,date:Date,status:String,description:String,intent: Context){
    val complainHistoryModel = complainHistoryModel(
        img,
        type,
        title,
        date,
        status,
        description,
        "default 111, check ComplainRaiseHistory > createComplainHistory()"
    )

    val fireStore = Firebase.firestore
    val auth = Firebase.auth

    val user = auth.currentUser

    fireStore.collection("member").document(user!!.uid)
        .get().addOnSuccessListener { document ->
            run {
                val societyId = document.data!!["societyId"] as String
                fireStore.collection("societies").document(societyId)
                    .collection("complain").document().set(complainHistoryModel)
                    .addOnSuccessListener {
                        Toast.makeText(intent, "Complain Created", Toast.LENGTH_LONG).show()
                    }
            }
        }
}
