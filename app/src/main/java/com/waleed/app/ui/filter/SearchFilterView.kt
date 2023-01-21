package com.waleed.app.ui.filter

import com.waleed.app.business.data.SearchFilterCategory
import com.waleed.app.business.data.newdata.FilterResponse
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface SearchFilterView : BaseView {
    fun showSearchCategory(listData: List<SearchFilterCategory>)
    fun showError(message: String)
    fun setUpFilterValues(response: FilterResponse)
    fun onProductListSuccess(list: ArrayList<ProductItemData>)
    fun onProductListEmpty()
}