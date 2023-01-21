package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class CartCountResponse(
    @SerializedName("CartCount")
    var cartCount: Int = 0,
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
)