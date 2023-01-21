package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class AgeGroupProductListResponse(
    @SerializedName("AgeGroupID")
    var ageGroupID: Int = 0,
    @SerializedName("AgeGroupName")
    var ageGroupName: String = "",
    @SerializedName("AgeGroupNameAr")
    var ageGroupNameAr: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ProductList")
    var productItem: ArrayList<ProductItemData> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
)