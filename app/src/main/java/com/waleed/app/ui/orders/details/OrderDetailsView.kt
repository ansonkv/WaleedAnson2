package com.waleed.app.ui.orders.details

import com.waleed.app.business.data.OrderProductData
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.base.BaseView

interface OrderDetailsView :BaseView {
    fun showListData(listData: List<MyOrderResponse.OrderItem.OrderedProductItem>)

}