package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("SearchList")
    var searchItemList: ArrayList<SearchItem> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0
) {
    data class SearchItem(
        @SerializedName("Item")
        var item: String = "",
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("ItemImage")
        var imageUrl: String = ""
    ) : Serializable
}