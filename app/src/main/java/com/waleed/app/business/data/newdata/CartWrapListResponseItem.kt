package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CartWrapListResponseItem(
    @SerializedName("PaperID")
    var paperID: Int = 0,
    @SerializedName("WrappingCharge")
    var wrappingCharge: Double = 0.0,
    @SerializedName("WrappingChargeKD")
    var wrappingChargeKD: String = "",
    @SerializedName("WrappingChargeKDAr")
    var wrappingChargeKDAr: String = ""
):Serializable
