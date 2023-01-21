package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class RefundPolicyResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("RefundPolicy")
    var refundPolicy: RefundPolicy = RefundPolicy(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class RefundPolicy(
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("DescriptionAr")
        var descriptionAr: String = ""
    )
}