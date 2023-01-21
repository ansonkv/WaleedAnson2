package com.waleed.app.ui.account.edit

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class EditProfilePresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<EditProfileView>() {
    fun validateData(name: String, phone: String) {
        if (name.isNullOrEmpty()) {
            view?.emptyName()
        } else if (phone.isNullOrEmpty()||phone.length<8) {
            view?.emptyPhone()
        } else {
            updateProfile(name, phone)
        }
    }

    private fun updateProfile(name: String, phone: String) {
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
                        view?.onSuccessUpdate()
                    } else
                        view?.showPop(it.message)
                }, {})
        } else
            view?.showNoNetwork()
    }

}