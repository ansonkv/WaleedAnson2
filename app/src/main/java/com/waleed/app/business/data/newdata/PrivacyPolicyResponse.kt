package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class PrivacyPolicyResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("PrivacyPolicy")
    var privacyPolicy: PrivacyPolicy = PrivacyPolicy(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class PrivacyPolicy(
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("DescriptionAr")
        var descriptionAr: String = ""
    )
}