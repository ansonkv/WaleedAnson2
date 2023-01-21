package com.waleed.app.ui.subcategory

import com.waleed.app.business.data.newdata.SubCategoryListResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface SubCategoryView:BaseView {
    fun showSubCategoryList(subCategoryListResponse: ArrayList<SubCategoryListResponse.SubCategoryData>)
    fun showEmptyList()
}