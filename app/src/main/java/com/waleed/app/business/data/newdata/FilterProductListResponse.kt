package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class FilterProductListResponse(
    @SerializedName("List")
    var list: ArrayList<ProductItemData> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
)