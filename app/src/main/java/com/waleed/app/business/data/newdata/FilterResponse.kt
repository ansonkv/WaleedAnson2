package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class FilterResponse(
    @SerializedName("AgeGroupList")
    var ageGroupValueList: ArrayList<AgeGroupValue> = arrayListOf(),
    @SerializedName("BrandList")
    var brandValueList: ArrayList<BrandValue> = arrayListOf(),
    @SerializedName("IdealList")
    var idealList: ArrayList<Ideal> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Price")
    var price: Price = Price(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class AgeGroupValue(
        @SerializedName("AgeGroupID")
        var ageGroupID: Int = 0,
        @SerializedName("IdealID")
        var idealId: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("NameAr")
        var nameAr: String = ""
    )

    data class BrandValue(
        @SerializedName("BrandID")
        var brandID: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("NameAr")
        var nameAr: String = ""
    )

    data class Ideal(
        @SerializedName("IdealID")
        var idealID: Int = 0,
        @SerializedName("Name")
        var name: String = "",
        @SerializedName("NameAr")
        var nameAr: String = ""
    )

    data class Price(
        @SerializedName("MaxValue")
        var maxValue: Double = 0.0,
        @SerializedName("MaxValueKD")
        var maxValueKD: String = "",
        @SerializedName("MaxValueKDAr")
        var maxValueKDAr: String = "",
        @SerializedName("MinValue")
        var minValue: Double = 0.0,
        @SerializedName("MinValueKD")
        var minValueKD: String = "",
        @SerializedName("MinValueKDAr")
        var minValueKDAr: String = ""
    )
}