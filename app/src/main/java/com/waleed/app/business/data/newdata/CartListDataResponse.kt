package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartListDataResponse(
    @SerializedName("CartItems")
    var cartItems: ArrayList<CartItem> = arrayListOf(),
    @SerializedName("CouponDiscount")
    var couponDiscount: String = "",
    @SerializedName("CouponDiscountKD")
    var couponDiscountKD: String = "",
    @SerializedName("CouponDiscountKDAr")
    var couponDiscountKDAr: String = "",
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("DeviceTokenID")
    var deviceTokenID: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("SubTotal")
    var subTotal: String = "",
    @SerializedName("SubTotalKD")
    var subTotalKD: String = "",
    @SerializedName("SubTotalKDAr")
    var subTotalKDAr: String = "",
    @SerializedName("Total")
    var total: String = "",
    @SerializedName("TotalKD")
    var totalKD: String = "",
    @SerializedName("TotalKDAr")
    var totalKDAr: String = "",
    @SerializedName("WrapCharges")
    var wrapCharges: ArrayList<CartWrapListResponseItem> = arrayListOf(),
    @SerializedName("WrappingTotal")
    var wrappingTotal: String = "",
    @SerializedName("WrappingTotalKD")
    var wrappingTotalKD: String = "",
    @SerializedName("WrappingTotalKDAr")
    var wrappingTotalKDAr: String = ""
) : Serializable {
    data class CartItem(
        @SerializedName("CartID")
        var cartID: Int = 0,
        @SerializedName("Combination")
        var combination: Combination = Combination(),
        @SerializedName("CombinationID")
        var combinationID: Int = 0,
        @SerializedName("PaperID")
        var paperID: Int = 0,
        @SerializedName("PaperImage")
        var paperImage: String? = "",
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ProductNameAr")
        var productNameAr: String = "",
        @SerializedName("SelectedQuantity")
        var selectedQuantity: Int = 0,
        @SerializedName("StoreID")
        var storeID: Int = 0,
        @SerializedName("StoreName")
        var storeName: String = "",
        @SerializedName("StoreNameAr")
        var storeNameAr: String = "",
        @SerializedName("ThumbImage")
        var thumbImage: String = "",
        @SerializedName("UnitPrice")
        var unitPrice: String = "",
        @SerializedName("UnitPriceAr")
        var unitPriceAr: String = "",
        @SerializedName("UnitsAvailable")
        var unitsAvailable: Int = 0
    ) : Serializable {
        data class Combination(
            @SerializedName("ColorCode")
            var colorCode: String = "",
            @SerializedName("ColorID")
            var colorID: Int = 0,
            @SerializedName("ColorName")
            var colorName: String = "",
            @SerializedName("ColorNameAr")
            var colorNameAr: String = "",
            @SerializedName("SizeCode")
            var sizeCode: String = "",
            @SerializedName("SizeID")
            var sizeID: Int = 0,
            @SerializedName("SizeName")
            var sizeName: String = "",
            @SerializedName("SizeNameAr")
            var sizeNameAr: String = "",
            @SerializedName("VariationID")
            var variationID: Int = 0,
            @SerializedName("VariationName")
            var variationName: String = "",
            @SerializedName("VariationNameAr")
            var variationNameAr: String = ""
        ) : Serializable
    }
}