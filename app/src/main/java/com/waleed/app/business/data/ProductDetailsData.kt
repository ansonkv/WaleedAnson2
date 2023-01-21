package com.waleed.app.business.data

data class ProductDetailsData(
    var productId: Int,
    var productName: String,
    var productPrice: Double,
    var isInStock: Boolean,
    var rating: Double,
    var reviewCount: Int,
    var productDescription:String,
    var isSizeColorAvailable: Boolean,
    var featuredAge: String,
    var featuredCategory:String,
    var reviewImageList: List<String>,
    var reviewDataList: List<ReviewData>,
    var productTagsList: List<ProductTags>,
    var sizeDataList: List<SizeData>,
    var productImageList:ArrayList<String>,
    var productBrand:String,
    var productManufracutrer:String
)

data class ReviewData(
    var reviewId: Int,
    var reviewName: String,
    var reviewDetails: String,
    var reviewDate: String,
    var reviewImageUrlList: List<String>
)

data class ProductTags(var tagId: Int, var tagName: String)

data class ColorData(var colorId: Int, var colorCode: String)

data class SizeData(
    var sizeId: Int,
    var sizeName: Int,
    var sizeType: String,
    var colorList: List<ColorData>
)