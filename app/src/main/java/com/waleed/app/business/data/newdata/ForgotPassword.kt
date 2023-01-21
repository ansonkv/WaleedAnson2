package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class ForgotPassword(
    @SerializedName("Message")
    var message: String,
    @SerializedName("Status")
    var status: Int
)