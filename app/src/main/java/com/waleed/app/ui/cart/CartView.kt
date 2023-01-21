package com.waleed.app.ui.cart

import com.waleed.app.business.data.newdata.CartListDataResponse
import com.waleed.app.ui.base.BaseView

interface CartView : BaseView {
    fun showCartListData(cartListDataResponse: CartListDataResponse)
    fun removeCartFailed()
    fun itemRemovedSuccessfully(productID: Int, combinationID: Int)
    fun showEmptyList()
    fun onStoreUpdatedSuccessfully()
    fun onWrapperRemove()
}