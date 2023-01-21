package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class MyOrderTEst(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Orders")
    var orders: List<Order> = listOf(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class Order(
        @SerializedName("CouponCode")
        var couponCode: Any = Any(),
        @SerializedName("CustomerID")
        var customerID: Int = 0,
        @SerializedName("DeliveryAddress")
        var deliveryAddress: Any = Any(),
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
        var products: List<Product> = listOf(),
        @SerializedName("TotalAmt")
        var totalAmt: String = "",
        @SerializedName("TransactionID")
        var transactionID: String = "",
        @SerializedName("UniqueOrderID")
        var uniqueOrderID: String = "",
        @SerializedName("Wrapping")
        var wrapping: Any = Any()
    ) {
        data class Product(
            @SerializedName("ClosingTime")
            var closingTime: Any = Any(),
            @SerializedName("OpeningTime")
            var openingTime: Any = Any(),
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
            var storeStatus: Any = Any(),
            @SerializedName("StoreStatusAr")
            var storeStatusAr: Any = Any(),
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
            var wrapMessage: Any = Any()
        )
    }
}