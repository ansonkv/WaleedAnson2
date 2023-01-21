package com.waleed.app.business.data.newdata

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductStore(
    @SerializedName("Area")
    var area: String = "",
    @SerializedName("AreaAr")
    var areaAr: String = "",
    @SerializedName("ClosingTime")
    var closingTime: String = "",
    @SerializedName("Latitude")
    var latitude: String = "",
    @SerializedName("Longitude")
    var longitude: String = "",
    @SerializedName("OpeningTime")
    var openingTime: String = "",
    @SerializedName("Phone")
    var phone: String = "",
    @SerializedName("ProductID")
    var productID: Int = 0,
    @SerializedName("StoreAddress")
    var storeAddress: String = "",
    @SerializedName("StoreAddressAr")
    var storeAddressAr: String = "",
    @SerializedName("StoreID")
    var storeID: Int = 0,
    @SerializedName("StoreStatus")
    var storeStatus: String = "",
    @SerializedName("StoreStatusAr")
    var storeStatusAr: String = "",
    var isStoreSelected: Boolean =false
):Serializable