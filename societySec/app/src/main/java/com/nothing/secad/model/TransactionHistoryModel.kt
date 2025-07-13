package com.nothing.secad.model

import java.util.Date

data class TransactionHistoryModel(
    var date: Date,
    var amount: Int,
    var status: Boolean,
    val id: String
)
