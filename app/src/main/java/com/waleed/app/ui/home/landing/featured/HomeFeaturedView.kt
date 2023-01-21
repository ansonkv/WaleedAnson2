package com.waleed.app.ui.home.landing.featured

import com.waleed.app.business.data.newdata.FeaturedProductListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView

interface HomeFeaturedView : BaseView {

    fun showFeaturedProductLists(featureListResponse: List<FeaturedProductListResponse.Feature>)
    fun showLoaderInList()
    fun hideLoaderInList()
    fun showEmptyList()
    fun onFavAddedSuccess(data: ProductItemData)
    fun onFavRemoveSuccess(data: ProductItemData)
    fun onCartAddedSuccessfully(addToCartResponse: ProductItemData)
    fun onFeaturedLoading()
    fun onFeaturedLoadingFinished()
}