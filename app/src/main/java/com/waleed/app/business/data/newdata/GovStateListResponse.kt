package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class GovStateListResponse(
    @SerializedName("CountryID")
    var countryID: Int = 0,
    @SerializedName("GovStateList")
    var govStateList: ArrayList<GovState> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class GovState(
        @SerializedName("GovStateID")
        var govStateID: Int = 0,
        @SerializedName("GovStateName")
        var govStateName: String = "",
        @SerializedName("GovStateNameAr")
        var govStateNameAr: String = "",
        @SerializedName("Show")
        var show: Int = 0
    )
}