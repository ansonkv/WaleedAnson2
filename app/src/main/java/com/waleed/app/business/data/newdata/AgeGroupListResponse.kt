package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class AgeGroupListResponse(
    @SerializedName("List")
    var ageGroupList: ArrayList<AgeGroupData> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class AgeGroupData(
        @SerializedName("AgeGroupID")
        var ageGroupID: Int = 0,
        @SerializedName("AgeGroupName")
        var ageGroupName: String = "",
        @SerializedName("AgeGroupNameAr")
        var ageGroupNameAr: String = "",
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("ImageMobile")
        var imageMobile: String = "",
        @SerializedName("Show")
        var show: Int = 0
    )
}