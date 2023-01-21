package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class TermsConditionsResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("TermsConditions")
    var termsConditions: TermsConditions = TermsConditions()
) {
    data class TermsConditions(
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("DescriptionAr")
        var descriptionAr: String = ""
    )
}