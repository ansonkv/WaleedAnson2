package com.waleed.app.ui.productdetails

import com.waleed.app.business.data.newdata.AddToCartResponse
import com.waleed.app.business.data.newdata.ProductDetailsResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView

interface ProductDetailsView : BaseView {
    fun showProductDetails(detailsResponse: ProductDetailsResponse)
    fun onCartItemAddSuccess(cartCount: Int, isFromList: Boolean, productItemData: ProductItemData)
    fun onFavAddedSuccess()
    fun onFavRemoveSuccess()
    fun onFavAddedSuccess(data: ProductItemData, position: Int)
    fun onFavRemoveSuccess(data: ProductItemData, position: Int)
    fun onAddedToCartFromList(cartCountResponse: AddToCartResponse)
}