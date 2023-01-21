package com.waleed.app.business.data

data class NotificationData(
    var id: Int,
    val title: String,
    var time:String,
    var msg: String,
    var type:Int,
    var isDelivered: Boolean
)