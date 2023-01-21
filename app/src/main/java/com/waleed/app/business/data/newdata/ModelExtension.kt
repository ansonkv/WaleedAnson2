package com.waleed.app.business.data.newdata

import com.waleed.app.util.LangUtils

fun ProductItemData.getProductPrice(): String {
    return LangUtils.getLanguageString(this.discountUnitPrice, this.discountUnitPriceAr)
}


fun ProductItemData.getProductName(): String {
    return LangUtils.getLanguageString(this.productName, this.productNameAr)
}

fun ProductItemData.getUnitPrice(): String {
    return LangUtils.getLanguageString(this.unitPrice, this.unitPriceAr)
}

fun ProductDetailsResponse.Product.getProductName(): String {
    return LangUtils.getLanguageString(this.productName, this.productNameAr)
}

fun ProductDetailsResponse.Product.getProductPrice(): String {
    return LangUtils.getLanguageString(this.discountUnitPrice, this.discountUnitPriceAr)
}