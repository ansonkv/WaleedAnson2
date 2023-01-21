package com.waleed.app.ui.home

import android.util.Log
import com.waleed.app.Waleed
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<HomeView>() {
    fun getCartCount() {
        if (isConnected) {
            Log.e("Device Id>>>",Waleed.getInstance().getAppPref()
                .getString(AppConstants.PREF_DEVICE_TOKEN)!!)
            apiDataSource.getCartCount(
                deviceId = Waleed.getInstance().getAppPref()
                    .getString(AppConstants.PREF_DEVICE_TOKEN)!!,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                },
                appType = 2
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    if (it.status != 0) {
                        view?.onCartItemAddSuccess(it.cartCount)
                    } else {

                    }
                }, {

                })
        }
    }

}