package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class IdealProductListResponse(
    @SerializedName("IdealID")
    var idealID: Int = 0,
    @SerializedName("IdealName")
    var idealName: String = "",
    @SerializedName("IdealNameAr")
    var idealNameAr: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ProductList")
    var productItem: ArrayList<ProductItemData> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
)