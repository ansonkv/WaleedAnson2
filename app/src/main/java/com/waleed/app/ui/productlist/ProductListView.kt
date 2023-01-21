package com.waleed.app.ui.productlist

import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView

interface ProductListView : BaseView {
    fun showProductList(listDataResponse: ArrayList<ProductItemData>)
    fun showEmptyList()
    fun onFavAddedSuccess(data: ProductItemData, position: Int)
    fun onFavRemoveSuccess(data: ProductItemData, position: Int)
    fun onCartAddedSuccessfully(addToCartResponse: ProductItemData)
    fun onCartItemAddSuccess(cartCount: Int)
}