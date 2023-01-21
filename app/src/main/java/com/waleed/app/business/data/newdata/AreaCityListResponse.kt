package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class AreaCityListResponse(
    @SerializedName("AreaCityList")
    var areaCityList: ArrayList<AreaCity> = arrayListOf(),
    @SerializedName("CountryID")
    var countryID: Int = 0,
    @SerializedName("GovStateID")
    var govStateID: Int = 0,
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class AreaCity(
        @SerializedName("AreaCityID")
        var areaCityID: Int = 0,
        @SerializedName("AreaCityName")
        var areaCityName: String = "",
        @SerializedName("AreaCityNameAr")
        var areaCityNameAr: String = "",
        @SerializedName("Show")
        var show: Int = 0
    )
}