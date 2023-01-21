package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategoryListResponse(
    @SerializedName("List")
    var categoryList: List<CategoryListData>,
    @SerializedName("Message")
    var message: String,
    @SerializedName("Status")
    var status: Int
) {
    data class CategoryListData(
        @SerializedName("CategoryID")
        var categoryID: Int,
        @SerializedName("CategoryName")
        var categoryName: String,
        @SerializedName("CategoryNameAr")
        var categoryNameAr: String,
        @SerializedName("Image")
        var image: String,
        @SerializedName("Show")
        var show: Int,
        @SerializedName("HasSubCategory")
        var hasSubCategory: Int
    ) : Serializable
}