package com.waleed.app.ui.favourite

import com.waleed.app.business.data.newdata.FavoriteListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView
import kotlin.collections.ArrayList

interface FavouriteProductListView :BaseView{
    fun showProductList(productList: ArrayList<FavoriteListResponse.Favourite>)
    fun showEmptyList()
    fun onFavRemoveSuccess(data: ProductItemData, position: Int)
    fun onCartAddedSuccessfully(productItemData: ProductItemData)
    fun onCartItemAddSuccess(size: Int)

}