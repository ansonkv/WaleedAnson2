package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SubCategoryListResponse(
    @SerializedName("CategoryID")
    var categoryID: Int = 0,
    @SerializedName("List")
    var subCategoryList: ArrayList<SubCategoryData> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class SubCategoryData(
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("Show")
        var show: Int = 0,
        @SerializedName("SubCategoryID")
        var subCategoryID: Int = 0,
        @SerializedName("SubCategoryName")
        var subCategoryName: String = "",
        @SerializedName("SubCategoryNameAr")
        var subCategoryNameAr: String = ""
    ):Serializable
}