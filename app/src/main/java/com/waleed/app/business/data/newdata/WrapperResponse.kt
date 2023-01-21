package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class WrapperResponse(
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Papers")
    var papers: ArrayList<Paper> = arrayListOf(),
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("Tags")
    var tags: ArrayList<Tag> = arrayListOf()
) {
    data class Paper(
        @SerializedName("PaperID")
        var paperID: Int = 0,
        @SerializedName("PaperImage")
        var paperImage: String = ""
    )

    data class Tag(
        @SerializedName("TagID")
        var tagID: Int = 0,
        @SerializedName("TagName")
        var tagName: String = "",
        @SerializedName("TagNameAr")
        var tagNameAr: String = ""
    )
}