package com.waleed.app.ui.checkout

import com.waleed.app.business.data.newdata.*
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface CheckoutView : BaseView {
    fun showDeliverType(list: ArrayList<DeliveryTypeResponse.DeliveryType>)
    fun showDeliverTimes(list: ArrayList<DeliveryTimeListResponse.DeliveryTime>)
    fun onCheckOutSuccess(checkOutResponse: CheckOutResponse)
    fun applyDisCountValue(it: DiscountCouponResponse?)
    fun onMobleUpdateSuccess()
    fun showPaymentMethods(methodList: List<PaymentMethodsResponse.PaymentMethod>)
    fun showEmptyList()
    fun showAddressList(addressItemList: ArrayList<AddressListResponse.AddressItem>)
}