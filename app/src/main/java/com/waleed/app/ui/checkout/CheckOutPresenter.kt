package com.waleed.app.ui.checkout

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.GuestLoginResponse
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CheckOutPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<CheckoutView>() {
    fun getDeliveryType() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getDeliveryType().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showDeliverType(it.list)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }

    }

    fun getDeliveryTime() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getDeliveryTimeList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showDeliverTimes(it.list)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun makeCheckOut(
        addressId: Int, deliveryTimeId: Int, deliveryTypeId: Int,
        paymentMode: Int, subTotal: String, totalAmount: String, selectedDate: String,
        couponCode: String, couponDiscount: String, wrappingCharge: String
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.makeCheckOut(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID,
                addressId = addressId,
                applicationType = 3,
                couponCode = couponCode,
                couponDiscount = couponDiscount,
                customDate = selectedDate,
                deliveryTimeId = deliveryTimeId,
                deliveryTypeId = deliveryTypeId,
                paymentMode = paymentMode,
                subTotal = subTotal,
                totalAmount = totalAmount,
                wrappingCharge = wrappingCharge
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onCheckOutSuccess(it)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun applyDiscountCoupon(subTotal: String, couponCode: String) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.applyCoupon(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                subTotal = subTotal, couponCode = couponCode
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.applyDisCountValue(it)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else view?.showNoNetwork()
    }

    fun updateProfile(name: String, phone: String) {
        if (isConnected) {
            view?.showLoading()
            val callingCode = "+965"
            val namePart = name.toRequestBody(MultipartBody.FORM)
            var customerId = LoggedUser.getInstance().getAccount()!!.customerID.toString()
            val customerIdPart = customerId.toRequestBody(MultipartBody.FORM)
            val mobilePart = phone.toRequestBody(MultipartBody.FORM)
            val callingCodePart = callingCode.toRequestBody(MultipartBody.FORM)

            apiDataSource.updateProfile(
                customerID = customerIdPart,
                name = namePart,
                mobile = mobilePart,
                callingCode = callingCodePart
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        val customer = LoggedUser.getInstance().getAccount()
                        customer!!.customerName = it.customerName
                        customer!!.mobile = phone
                        customer!!.callingCode = callingCode
                        LoggedUser.getInstance().setAccount(customer)
                        view?.onMobleUpdateSuccess()
                    } else
                        view?.showPop(it.message)
                }, {})
        } else
            view?.showNoNetwork()
    }

    fun getPaymentMethods() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getPaymentMethods().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showPaymentMethods(it.list)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }

    }

    fun getAddressListForGuest() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getAddressList(
                (Waleed.appContext.getAppPref().loadObject(
                    AppConstants.PREF_GUEST_USER_DATA, GuestLoginResponse::class.java
                ) as GuestLoginResponse).customerID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.addressItemList.isNullOrEmpty()) {
                            view?.showEmptyList()
                        } else {
                            view?.showAddressList(it.addressItemList)
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun makeGuestCheckOut(
        deliveryTypeId: Int,
        totalAmount: String,
        subTotal: String,
        paymentMode: Int,
        deliveryTimeId: Int,
        addressId: Int,
        selectedDate: String,
        couponCode: String,
        wrappingCharge: String,
        couponDiscount: String
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.makeCheckOut(
                customerID = (Waleed.getInstance().getAppPref().loadObject(
                    AppConstants.PREF_GUEST_USER_DATA,
                    GuestLoginResponse::class.java
                ) as GuestLoginResponse).customerID,
                addressId = addressId,
                applicationType = 3,
                couponCode = couponCode,
                couponDiscount = couponDiscount,
                customDate = selectedDate,
                deliveryTimeId = deliveryTimeId,
                deliveryTypeId = deliveryTypeId,
                paymentMode = paymentMode,
                subTotal = subTotal,
                totalAmount = totalAmount,
                wrappingCharge = wrappingCharge
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onCheckOutSuccess(it)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }
}