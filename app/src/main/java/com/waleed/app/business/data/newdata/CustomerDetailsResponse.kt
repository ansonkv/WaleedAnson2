package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class CustomerDetailsResponse(
    @SerializedName("Customer")
    var customer: Customer,
    @SerializedName("Message")
    var message: String,
    @SerializedName("Status")
    var status: Int
) {
    data class Customer(
        @SerializedName("Active")
        var active: Int=0,
        @SerializedName("CallingCode")
        var callingCode: String="",
        @SerializedName("CustomerID")
        var customerID: Int=0,
        @SerializedName("CustomerName")
        var customerName: String="",
        @SerializedName("EmailID")
        var emailID: String="",
        @SerializedName("Mobile")
        var mobile: String="",
        @SerializedName("Photo")
        var photo: String="",
        @SerializedName("PhotoMobile")
        var photoMobile: String=""
    )
}