package com.nothing.secad.model

import java.util.Date

data class userTransaction(
    var date: Date,
    var amount: Int,
    var status: Boolean,
    var homeNo: String,
    val id: String
)
