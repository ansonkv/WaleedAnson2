package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class HomeSliderDataResponse(
    @SerializedName("List")
    var homeSliderDataList: List<HomeSliderData>,
    @SerializedName("Message")
    var message: String,
    @SerializedName("Status")
    var status: Int
) {
    data class HomeSliderData(
        @SerializedName("CategoryID")
        var categoryID: Int = 0,
        @SerializedName("CategoryName")
        var categoryName: String = "",
        @SerializedName("CategoryNameAr")
        var categoryNameAr: String = "",
        @SerializedName("ImageMobile")
        var imageMobile: String = "",
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("ShowCategory")
        var showCategory: Int = 0,
        @SerializedName("ShowHome")
        var showHome: Int = 0,
        @SerializedName("SliderID")
        var sliderID: Int = 0
    )
}