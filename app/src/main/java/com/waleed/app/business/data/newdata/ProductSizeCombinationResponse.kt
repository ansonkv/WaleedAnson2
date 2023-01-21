package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class ProductSizeCombinationResponse(
    @SerializedName("ColorCode")
    var colorCode: String = "",
    @SerializedName("ColorID")
    var colorID: Int = 0,
    @SerializedName("ColorName")
    var colorName: String = "",
    @SerializedName("ColorNameAr")
    var colorNameAr: String = "",
    @SerializedName("DesignID")
    var designID: Int = 0,
    @SerializedName("ProductID")
    var productID: Int = 0,
    @SerializedName("Sizes")
    var sizes: List<Size> = listOf()
) {
    data class Size(
        @SerializedName("CombinationID")
        var combinationID: Int = 0,
        @SerializedName("DesignID")
        var designID: Int = 0,
        @SerializedName("DiscountUnitPrice")
        var discountUnitPrice: String = "",
        @SerializedName("DiscountUnitPriceAr")
        var discountUnitPriceAr: String = "",
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("SizeCode")
        var sizeCode: String = "",
        @SerializedName("SizeID")
        var sizeID: Int = 0,
        @SerializedName("SizeName")
        var sizeName: String = "",
        @SerializedName("SizeNameAr")
        var sizeNameAr: String = "",
        @SerializedName("UnitPrice")
        var unitPrice: String = "",
        @SerializedName("UnitPriceAr")
        var unitPriceAr: String = ""
    )
}