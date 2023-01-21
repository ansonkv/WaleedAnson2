package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class GuestLoginResponse(
    @SerializedName("CustomerAddressID")
    var customerAddressID: Int = 0,
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
)