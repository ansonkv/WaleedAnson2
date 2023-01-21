package com.waleed.app.ui.home.category

import com.waleed.app.business.data.newdata.CategoryListResponse
import com.waleed.app.business.data.newdata.HomeSliderDataResponse
import com.waleed.app.ui.base.BaseView

interface CategoryView:BaseView {
    fun loadOfferSliderData(offerListData: List<HomeSliderDataResponse.HomeSliderData>)
    fun showCategoryList(listData: List<CategoryListResponse.CategoryListData>)
}