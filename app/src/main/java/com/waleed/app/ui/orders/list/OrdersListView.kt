package com.waleed.app.ui.orders.list

import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.base.BaseView

interface OrdersListView : BaseView {
    fun showOrderList(listOf: ArrayList<MyOrderResponse.OrderItem>)
    fun showEmptyList()
}