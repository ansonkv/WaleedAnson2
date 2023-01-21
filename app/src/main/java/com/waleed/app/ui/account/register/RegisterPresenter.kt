package com.waleed.app.ui.account.register

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.RegisterResponse
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<SignUpView>() {
    fun registerAccount(
        name: String, email: String, password: String, confirmPass: String,
        phone: String, selectedCountryCode: String, loginType: Int
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.registerAccount(
                name, email, password, confirmPass, phone, selectedCountryCode, loginType,
                Waleed.getInstance().getAppPref().getString(AppConstants.PREF_DEVICE_TOKEN)!!,2
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        getCustomerDetails(it)
                    } else if(it.status==2){
                        view?.hideLoading()
                        view?.onExistingEmailId()
                    } else{
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                }, {
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }

    private fun getCustomerDetails(response: RegisterResponse) {
        if (isConnected) {
            apiDataSource.getCustomerDetails(response.customerID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        LoggedUser.getInstance().setAccount(it.customer)
                        view?.onSuccessLogin(response)
                        view?.hideLoading()
                    }
                }, {
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }
}