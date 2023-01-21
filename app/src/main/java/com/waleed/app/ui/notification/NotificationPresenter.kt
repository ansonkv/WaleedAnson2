package com.waleed.app.ui.notification

import com.waleed.app.business.data.NotificationData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NotificationPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<NotificationView>() {


    fun getNotification() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getNotification(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.list.isNullOrEmpty()) {
                            view?.showEmptyNotification()
                        } else {
                            view?.showNotificationList(it.list)
                        }
                    }
                }, {})

        } else
            view?.showNoNetwork()
    }

}