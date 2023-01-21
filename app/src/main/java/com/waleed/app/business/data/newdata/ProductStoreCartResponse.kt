package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class ProductStoreCartResponse(
    @SerializedName("CartID")
    var cartID: Int = 0,
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ProductStores")
    var productStores: ArrayList<ProductStore> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
)