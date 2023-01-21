package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class SocialMediaReponse(
    @SerializedName("Links")
    var links: Links = Links(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("ContactInfo")
    var contactInfo: ContactInfo = ContactInfo()
) {
    data class Links(
        @SerializedName("FacebookLink")
        var facebookLink: String = "",
        @SerializedName("InstagramLink")
        var instagramLink: String = "",
        @SerializedName("LinkedInLink")
        var linkedInLink: String = "",
        @SerializedName("TwitterLink")
        var twitterLink: String = ""
    )
    data class ContactInfo(
        @SerializedName("Email")
        var email: String = "",
        @SerializedName("Mobile")
        var mobile: String = "",
        @SerializedName("Telephone")
        var telephone: String = ""
    )
}