package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class FeaturedProductListResponse(
    @SerializedName("FeatureList")
    var featureList: List<Feature> = listOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class Feature(
        @SerializedName("FeatureID")
        var featureID: Int = 0,
        @SerializedName("FeatureName")
        var featureName: String = "",
        @SerializedName("FeatureNameAr")
        var featureNameAr: String = "",
        @SerializedName("ProductList")
        var productList: List<ProductItemData> = listOf()
    )
}