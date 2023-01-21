package com.waleed.app.business.data.newdata

import com.google.gson.annotations.SerializedName
import com.waleed.app.util.LangUtils
import java.io.Serializable

data class ProductItemData(
    @SerializedName("DiscountUnitPrice")
    var discountUnitPrice: String = "",
    @SerializedName("DiscountUnitPriceAr")
    var discountUnitPriceAr: String = "",
    @SerializedName("Favorite")
    var favorite: Int = 0,
    @SerializedName("Points")
    var points: Int = 0,
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
    var unitPriceAr: String = "",
    @SerializedName("UnitsAvailable")
    var unitsAvailable: Int = 0
):Serializable