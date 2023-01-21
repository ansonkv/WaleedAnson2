package com.waleed.app.ui.account.changepass

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePassPresenter @Inject constructor(private var apiDataSource: APIDataSource):BasePresenter<ChangePassView>() {
    fun updatePassword(currentPass: String, newPass: String, confirmNewPass: String) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.updatePassword(LoggedUser.getInstance().getAccount()!!.customerID,currentPass,newPass,confirmNewPass).subscribeOn(
                Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.hideLoading()
                        view?.onSuccessPasswordChange(it)
                    } else {
                        view?.hideLoading()
                        view?.onFailedPasswordChange(it)
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