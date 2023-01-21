package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class FavoriteListResponse(
    @SerializedName("List")
    var list: ArrayList<Favourite> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class Favourite(
        @SerializedName("FavoriteID")
        var favoriteID: Int = 0,
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ProductNameAr")
        var productNameAr: String = "",
        @SerializedName("ThumbImage")
        var thumbImage: String = "",
        @SerializedName("UnitPrice")
        var unitPrice: String = "",
        @SerializedName("UnitPriceAr")
        var unitPriceAr: String = ""
    )
}