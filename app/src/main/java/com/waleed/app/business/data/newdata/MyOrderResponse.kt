package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyOrderResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Orders")
    var orderItems: ArrayList<OrderItem> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class OrderItem(
        @SerializedName("CouponCode")
        var couponCode: String = "",
        @SerializedName("CustomerID")
        var customerID: Int = 0,
        @SerializedName("DeliveryAddress")
        var deliveryAddress: String = "",
        @SerializedName("DeliveryAmt")
        var deliveryAmt: String = "",
        @SerializedName("DiscountAmt")
        var discountAmt: String = "",
        @SerializedName("NetAmt")
        var netAmt: String = "",
        @SerializedName("NetAmtAr")
        var netAmtAr: String = "",
        @SerializedName("OrderDate")
        var orderDate: String = "",
        @SerializedName("OrderID")
        var orderID: Int = 0,
        @SerializedName("OrderStatusAR")
        var orderStatusAR: String = "",
        @SerializedName("OrderStatusEN")
        var orderStatusEN: String = "",
        @SerializedName("OrderStatusID")
        var orderStatusID: Int = 0,
        @SerializedName("PaymentMode")
        var paymentMode: String = "",
        @SerializedName("PaymentStatus")
        var paymentStatus: String = "",
        @SerializedName("Products")
        var orderedProductItems: ArrayList<OrderedProductItem> = arrayListOf(),
        @SerializedName("TotalAmt")
        var totalAmt: String = "",
        @SerializedName("TransactionID")
        var transactionID: String = "",
        @SerializedName("UniqueOrderID")
        var uniqueOrderID: String = "",
        @SerializedName("Wrapping")
        var wrapping: ArrayList<WrappingData> = arrayListOf()
    ) : Serializable {
        data class OrderedProductItem(
            @SerializedName("ClosingTime")
            var closingTime: String = " ",
            @SerializedName("OpeningTime")
            var openingTime: String = " ",
            @SerializedName("OrderID")
            var orderID: Int = 0,
            @SerializedName("PaperID")
            var paperID: Int = 0,
            @SerializedName("PaperImage")
            var paperImage: String = "",
            @SerializedName("ProductID")
            var productID: Int = 0,
            @SerializedName("ProductName")
            var productName: String = "",
            @SerializedName("ProductNameAr")
            var productNameAr: String = "",
            @SerializedName("Quantity")
            var quantity: Int = 0,
            @SerializedName("StoreAddress")
            var storeAddress: String = "",
            @SerializedName("StoreID")
            var storeID: Int = 0,
            @SerializedName("StoreStatus")
            var storeStatus: String = " ",
            @SerializedName("StoreStatusAr")
            var storeStatusAr: String = " ",
            @SerializedName("TagID")
            var tagID: Int = 0,
            @SerializedName("TagName")
            var tagName: String = "",
            @SerializedName("TagNameAr")
            var tagNameAr: String = "",
            @SerializedName("ThumbImage")
            var thumbImage: String = "",
            @SerializedName("UnitPrice")
            var unitPrice: String = "",
            @SerializedName("WrapMessage")
            var wrapMessage: String = " "
        ) : Serializable

        data class WrappingData(
            @SerializedName("WrappingAmt")
            var wrappingAmt: String = "",
            @SerializedName("WrappingAmtAr")
            var wrappingAmtAr: String = ""
        ) : Serializable
    }
}