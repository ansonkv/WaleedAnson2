package com.waleed.app.ui.account.terms

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TermsPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<TermsView>() {
    fun getTermsAndCondition() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getTermsAndConditions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.displayTermsAndConditions(it.termsConditions)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {

                })
        } else
            view?.showNoNetwork()
    }

    fun getPrivacyPolicy() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getPrivacyPolicy()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.displayPrivacyPolicy(it.privacyPolicy)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {

                })
        } else
            view?.showNoNetwork()
    }

    fun getRefund() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getRefund()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.displayRefundPolicy(it.refundPolicy)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {

                })
        } else
            view?.showNoNetwork()
    }
}