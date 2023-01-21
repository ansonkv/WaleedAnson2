package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CheckOutResponse(
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("DeliveryDate")
    var deliveryDate: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("NetAmt")
    var netAmt: String = "",
    @SerializedName("NetAmtAr")
    var netAmtAr: String = "",
    @SerializedName("OrderDate")
    var orderDate: String = "",
    @SerializedName("OrderID")
    var orderID: Int = 0,
    @SerializedName("PaymentMethodCode")
    var paymentMethodCode: String = "",
    @SerializedName("PaymentMethodName")
    var paymentMethodName: String = "",
    @SerializedName("PaymentMode")
    var paymentMode: Int = 0,
    @SerializedName("PaymentURL")
    var paymentURL: String = "",
    @SerializedName("SessionID")
    var sessionID: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("TransactionDate")
    var transactionDate: String = "",
    @SerializedName("TransactionID")
    var transactionID: String = "",
    @SerializedName("UniqueOrderID")
    var uniqueOrderID: String = ""
) : Serializable