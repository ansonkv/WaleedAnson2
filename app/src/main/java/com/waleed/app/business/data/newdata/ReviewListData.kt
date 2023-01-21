package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReviewListData(
    @SerializedName("CustomerID")
    var customerID: Int = 0,
    @SerializedName("CustomerName")
    var customerName: String = "",
    @SerializedName("PhotoMobile")
    var photoMobile: String = "",
    @SerializedName("Rating")
    var rating: Double = 0.0,
    @SerializedName("Review")
    var review: String = "",
    @SerializedName("ReviewDate")
    var reviewDate: String = "",
    @SerializedName("ReviewID")
    var reviewID: Int = 0,
    @SerializedName("ReviewImages")
    var reviewImages: List<ReviewImage> = listOf()
) : Serializable {
    data class ReviewImage(
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("ReviewID")
        var reviewID: Int = 0
    ) : Serializable
}