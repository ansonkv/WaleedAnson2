package com.waleed.app.business.data.newdata


import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("AllReviewImages")
    var allReviewImages: ArrayList<ReviewListData.ReviewImage> = arrayListOf(),
    @SerializedName("Combinations")
    var colorCombinationResponseList: ArrayList<ProductSizeCombinationResponse> = arrayListOf(),
    @SerializedName("Images")
    var sliderImageList: ArrayList<SliderImageData> = arrayListOf(),
    @SerializedName("Message")
    var message: String = "",
    @SerializedName("Product")
    var product: Product = Product(),
    @SerializedName("ProductStores")
    var productStores: ArrayList<ProductStore> = arrayListOf(),
    @SerializedName("Reviews")
    var reviewList: ArrayList<ReviewListData> = arrayListOf(),
    @SerializedName("ReviewsCount")
    var reviewsCount: Int = 0,
    @SerializedName("Status")
    var status: Int = 0,
    @SerializedName("RelatedProductList")
    var relatedProductList: ArrayList<ProductItemData> = arrayListOf()
) {
    data class ColorCombination(
        @SerializedName("ColorCode")
        var colorCode: String = "",
        @SerializedName("ColorID")
        var colorID: Int = 0,
        @SerializedName("ColorName")
        var colorName: String = "",
        @SerializedName("ColorNameAr")
        var colorNameAr: String = "",
        @SerializedName("CombinationID")
        var combinationID: Int = 0,
        @SerializedName("DiscountUnitPrice")
        var discountUnitPrice: String = "",
        @SerializedName("DiscountUnitPriceAr")
        var discountUnitPriceAr: String = "",
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("SizeCode")
        var sizeCode: String = "",
        @SerializedName("SizeID")
        var sizeID: Int = 0,
        @SerializedName("SizeName")
        var sizeName: String = "",
        @SerializedName("SizeNameAr")
        var sizeNameAr: String = "",
        @SerializedName("UnitPrice")
        var unitPrice: String = "",
        @SerializedName("UnitPriceAr")
        var unitPriceAr: String = "",
        @SerializedName("VariationID")
        var variationID: Int = 0,
        @SerializedName("VariationName")
        var variationName: String = "",
        @SerializedName("VariationNameAr")
        var variationNameAr: String = ""
    )

    data class SliderImageData(
        @SerializedName("Image")
        var image: String = "",
        @SerializedName("ProductID")
        var productID: Int = 0
    )

    data class Product(
        @SerializedName("BrandID")
        var brandID: Int = 0,
        @SerializedName("BrandName")
        var brandName: String = "",
        @SerializedName("BrandNameAr")
        var brandNameAr: String = "",
        @SerializedName("CustomerID")
        var customerID: Int = 0,
        @SerializedName("Description")
        var description: String = "",
        @SerializedName("DescriptionAr")
        var descriptionAr: String = "",
        @SerializedName("DiscountUnitPrice")
        var discountUnitPrice: String = "",
        @SerializedName("DiscountUnitPriceAr")
        var discountUnitPriceAr: String = "",
        @SerializedName("Favorite")
        var favorite: Int = 0,
        @SerializedName("ProductID")
        var productID: Int = 0,
        @SerializedName("ProductName")
        var productName: String = "",
        @SerializedName("ProductNameAr")
        var productNameAr: String = "",
        @SerializedName("ProductShareUrl")
        var productShareUrl: String = "",
        @SerializedName("Rating")
        var rating: String = "",
        @SerializedName("Tags")
        var tags: String = "",
        @SerializedName("TagsAr")
        var tagsAr: String = "",
        @SerializedName("UnitPrice")
        var unitPrice: String = "",
        @SerializedName("UnitPriceAr")
        var unitPriceAr: String = "",
        @SerializedName("Points")
        var points: Int = 0,
        @SerializedName("AgeGroups")
        var ageGroups: String = "",
        @SerializedName("AgeGroupsAr")
        var ageGroupsAr: String = "",
        @SerializedName("ReplaceProduct")
        var replacementProduct: ProductItemData = ProductItemData(),
        @SerializedName("UnitsAvailable")
        var unitsAvailable: Int = 0,
        @SerializedName("Features")
        var featured: String = "",
        @SerializedName("FeaturesAr")
        var featuredAr: String = ""
    )
}