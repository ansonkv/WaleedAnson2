package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("CustomerName")
    var customerName: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("PhotoMobile")
    var photoMobile: String = "",
    @SerializedName("Status")
    var status: Int = 0
)