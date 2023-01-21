package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressListResponse(
    @SerializedName("AddressList")
    var addressItemList: ArrayList<AddressItem> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class AddressItem(
        @SerializedName("Active")
        var active: Int = 0,
        @SerializedName("AddressType")
        var addressType: Int = 0,
        @SerializedName("AddressTypeText")
        var addressTypeText: String = "",
        @SerializedName("AddressTypeTextAr")
        var addressTypeTextAr: String = "",
        @SerializedName("AreaCity")
        var areaCity: String = "",
        @SerializedName("AreaCityAr")
        var areaCityAr: String = "",
        @SerializedName("AreaCityID")
        var areaCityID: Int = 0,
        @SerializedName("Block")
        var block: String = "",
        @SerializedName("Building")
        var building: String = "",
        @SerializedName("CustomerAddressID")
        var customerAddressID: Int = 0,
        @SerializedName("CustomerID")
        var customerID: Int = 0,
        @SerializedName("Direction")
        var direction: String = "",
        @SerializedName("FlatNo")
        var flatNo: String = "",
        @SerializedName("Floor")
        var floor: String = "",
        @SerializedName("GovState")
        var govState: String = "",
        @SerializedName("GovStateAr")
        var govStateAr: String = "",
        @SerializedName("GovStateID")
        var govStateID: Int = 0,
        @SerializedName("HouseNo")
        var houseNo: String = "",
        @SerializedName("Latitude")
        var latitude: String = "",
        @SerializedName("Location")
        var location: String = "",
        @SerializedName("Longitude")
        var longitude: String = "",
        @SerializedName("Street")
        var street: String = "",
        @SerializedName("Title")
        var title: String = ""
    ):Serializable
}