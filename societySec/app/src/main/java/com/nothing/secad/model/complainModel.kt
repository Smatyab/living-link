package com.nothing.secad.model

import java.util.Date

data class complainModel(
    var imgUrl: String,
    var type:String,
    var title:String,
    var date: Date,
    var status:String,
    var description:String,
    var itemId: String
)
