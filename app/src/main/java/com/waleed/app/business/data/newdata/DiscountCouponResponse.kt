package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class DiscountCouponResponse(
    @SerializedName("CouponDiscount")
    var couponDiscount: String = "",
    @SerializedName("CouponDiscountAr")
    var couponDiscountAr: String = "",
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
)