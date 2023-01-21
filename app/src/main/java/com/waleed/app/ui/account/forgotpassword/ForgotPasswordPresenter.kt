package com.waleed.app.ui.account.forgotpassword

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ForgotPasswordPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<ForgotPasswordView>() {
    fun forgotPass(email: String) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.forgotPassword(email).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.hideLoading()
                        view?.onSubmitEmail(it)
                    } else {
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                }, {
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }
}