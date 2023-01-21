package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class PaymentMethodsResponse(
    @SerializedName("List")
    var list: List<PaymentMethod> = listOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class PaymentMethod(
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("NameAr")
        var nameAr: String = "",
        @SerializedName("PaymentMethodID")
        var paymentMethodID: Int = 0
    )
}