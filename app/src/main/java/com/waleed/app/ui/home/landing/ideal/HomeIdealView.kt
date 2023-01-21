package com.waleed.app.ui.home.landing.ideal

import com.waleed.app.business.data.newdata.AgeGroupListResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView
import kotlin.collections.ArrayList

interface HomeIdealView : BaseView {
    fun viewAgeGroupList(ageGroupList: List<AgeGroupListResponse.AgeGroupData>)
    fun showLoaderInList()
    fun hideLoaderInList()
    fun showIdealProductList(productItem: ArrayList<ProductItemData>)
    fun showEmptyList()
    fun onFavAddedSuccess(data: ProductItemData, position: Int)
    fun onFavRemoveSuccess(data: ProductItemData, position: Int)
    fun onCartAddedSuccessfully(addToCartResponse: ProductItemData)
    fun onProductLoadingFinished()
    fun onProductStartLoading()

}