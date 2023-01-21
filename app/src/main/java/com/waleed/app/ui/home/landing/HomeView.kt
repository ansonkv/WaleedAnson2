package com.waleed.app.ui.home.landing

import com.waleed.app.business.data.newdata.HomeSliderDataResponse
import com.waleed.app.business.data.newdata.IdealTypeListResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface HomeView : BaseView {

    fun loadOfferSliderData(offerListData: List<HomeSliderDataResponse.HomeSliderData>)

    fun showIdealCategoryList(idealTypeList: ArrayList<IdealTypeListResponse.IdealTypeData>)
}