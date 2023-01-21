package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class DeliveryTimeListResponse(
    @SerializedName("List")
    var list: ArrayList<DeliveryTime> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class DeliveryTime(
        @SerializedName("DelTimeID")
        var delTimeID: Int = 0,
        @SerializedName("Title")
        var title: String = "",
        @SerializedName("TitleAr")
        var titleAr: String = ""
    )
}