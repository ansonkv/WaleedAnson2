package com.waleed.app.ui.account.birthday.newchild

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewChildPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<NewChildView>() {
    fun addChildData(childName: String, bob: String) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.addNewChild(
                childName = childName,
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                dateOfBirth = bob
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.addedSuccessfully(it)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {
                    view?.hideLoading()
                })
        } else view?.showNoNetwork()
    }

    fun updateChildData(childName: String, bob: String, childId: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.updateChildData(
                childName = childName,
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                dateOfBirth = bob,
                childID = childId
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.addedSuccessfully(it)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {
                    view?.hideLoading()
                })
        } else
            view?.showNoNetwork()
    }
}