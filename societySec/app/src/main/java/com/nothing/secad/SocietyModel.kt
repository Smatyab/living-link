package com.nothing.secad

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import java.util.Date

data class Society(
    var name: String,
    var email: String,
    var password: String,
    var address: String,
    var uid: String,
    @field:JvmField
    var isBuilding: Boolean,

    var parking: Number,
    var elevators: Number,
    var watchMan: Number,
    var garden: Number,
    var temple: Number,
    var waterTank: Number,
    var totalHouses: Number,

    var expectedPricePerHouse: Number,
    var memberIDs: ArrayList<String>,

    var complains: ArrayList<Complain>
)

data class Transaction (
    var date: com.google.firebase.Timestamp,
    var amount: Number
)

data class Complain (
    var date: Date,
    var description: String,
    var approved: Boolean,
    var resolved: Boolean
)
