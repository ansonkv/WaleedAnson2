package com.waleed.app.ui.address.list

import com.waleed.app.business.data.newdata.AddressListResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface AddressListView : BaseView {
    fun showEmptyList()
    fun showAddressList(addressItemList: ArrayList<AddressListResponse.AddressItem>)
    fun onSuccessDelete(message: String)

}