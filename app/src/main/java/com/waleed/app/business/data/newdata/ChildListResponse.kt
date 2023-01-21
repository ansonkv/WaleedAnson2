package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChildListResponse(
    @SerializedName("List")
    var list: ArrayList<ChildItem> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0
) {
    data class ChildItem(
        @SerializedName("BirthDate")
        var birthDate: String = "",
        @SerializedName("ChildID")
        var childID: Int = 0,
        @SerializedName("ChildName")
        var childName: String = "",
        @SerializedName("CustomerID")
        var customerID: Int = 0
    ):Serializable
}