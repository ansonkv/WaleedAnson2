package com.waleed.app.ui.account.signin

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.RegisterResponse
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignInPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<SignInView>() {
    fun loginUser(email: String, password: String) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.userLogin(
                email, password,
                Waleed.getInstance().getAppPref().getString(AppConstants.PREF_DEVICE_TOKEN)!!,2
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        getCustomerDetails(it)
                    } else {
                        view?.hideLoading()
                        view?.onInvalidCredentials()
                    }
                }, {
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }

    private fun getCustomerDetails(response: RegisterResponse?) {
        if (isConnected) {
            apiDataSource.getCustomerDetails(response!!.customerID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        LoggedUser.getInstance().setAccount(it.customer)
                        view?.onSuccessLogin()
                        view?.hideLoading()
                    }
                }, {
                    view?.showErrorView()
                })
        }
    }

    fun socialLogin(id: String?, name: String?, loginType: Int) {
        if (isConnected){
            view?.showLoading()
            apiDataSource.socialLogin(
                email = id!!,name = name!!,loginType = loginType,
                deviceToken =  Waleed.getInstance().getAppPref().getString(AppConstants.PREF_DEVICE_TOKEN)!!,
                appType = 2
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        getCustomerDetails(it)
                    } else {
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                },{})
        }else
            view?.showNoNetwork()
    }
}