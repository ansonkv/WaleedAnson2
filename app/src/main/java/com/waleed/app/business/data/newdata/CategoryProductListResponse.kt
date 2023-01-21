package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class CategoryProductListResponse(
    @SerializedName("CategoryID")
    var categoryID: Int = 0,
    @SerializedName("CategoryName")
    var categoryName: String = "",
    @SerializedName("CategoryNameAr")
    var categoryNameAr: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("ProductList")
    var productList: ArrayList<ProductItemData> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
)