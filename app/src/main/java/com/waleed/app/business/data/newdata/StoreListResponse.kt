package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class StoreListResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("StoreList")
    var storeList: ArrayList<ProductStore> = arrayListOf()
)