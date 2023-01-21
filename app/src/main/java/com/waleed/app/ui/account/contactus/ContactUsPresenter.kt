package com.waleed.app.ui.account.contactus

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ContactUsPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<ContactUsView>() {
    fun submitContactUsForm(
        name: String,
        email: String,
        phone: String,
        reportType: Int,
        message: String
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.submitContactUsForm(
                callingCode = "965",
                mobile = phone, name = name, email = email,
                message = message, reportType = reportType
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onSuccessSubmit()
                    } else {
                        view?.showPop(it.message)

                    }
                }, {})
        }
    }

    fun getSocialMediaLinks() {
        if(isConnected){
            view?.showLoading()
            apiDataSource.getSocialLinks().subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status==1){
                        view?.setSocialLinks(it)
                    }
                },{
                    view?.hideLoading()
                })

        }else
            view?.showNoNetwork()
    }
}