package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class DeliveryTypeResponse(
    @SerializedName("List")
    var list: ArrayList<DeliveryType> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class DeliveryType(
        @SerializedName("DeliveryPrice")
        var deliveryPrice: String = "",
        @SerializedName("DeliveryPriceAr")
        var deliveryPriceAr: String = "",
        @SerializedName("DeliveryTypeID")
        var deliveryTypeID: Int = 0,
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("DescriptionAr")
        var descriptionAr: String = "",
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("NameAr")
        var nameAr: String = ""
    )
}