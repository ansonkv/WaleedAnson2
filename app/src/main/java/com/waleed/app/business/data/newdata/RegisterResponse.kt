package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("CustomerID")
    var customerID: Int,
    @SerializedName("Message")
    var message: String,
    @SerializedName("Status")
    var status: Int
)