package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class IdealTypeListResponse(
    @SerializedName("List")
    var idealTypeList: ArrayList<IdealTypeData> = arrayListOf<IdealTypeData>(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class IdealTypeData(
        @SerializedName("IdealID")
        var idealID: Int = 0,
        @SerializedName("IdealName")
        var idealName: String = "",
        @SerializedName("IdealNameAr")
        var idealNameAr: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("ImageMobile")
        var imageMobile: String = "",
        @SerializedName("Show")
        var show: Int = 0
    )
}