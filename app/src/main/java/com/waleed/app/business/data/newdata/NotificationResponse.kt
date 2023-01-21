package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("List")
    var list: ArrayList<NotificationItem> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class NotificationItem(
        @SerializedName("Message")
        var message: String = "",
        @SerializedName("OrderStatusID")
        var orderStatusID: String = "",
        @SerializedName("ShowReview")
        var showReview: Int = 0
    )
}