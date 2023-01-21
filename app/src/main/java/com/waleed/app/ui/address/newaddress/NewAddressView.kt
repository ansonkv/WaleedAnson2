package com.waleed.app.ui.address.newaddress

import com.waleed.app.business.data.newdata.AreaCityListResponse
import com.waleed.app.business.data.newdata.GovStateListResponse
import com.waleed.app.business.data.newdata.GuestLoginResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface NewAddressView : BaseView {
    fun setGovernorateList(govStateList: ArrayList<GovStateListResponse.GovState>)
    fun setAreaCityList(areaCityList: ArrayList<AreaCityListResponse.AreaCity>)
    fun onAddressAddedSuccess()
    fun onAddressUpdateSuccess()
    fun onGuestUserLoginFailed()
    fun onGuestUserLoginSuccess(guestLoginResponse: GuestLoginResponse)
}