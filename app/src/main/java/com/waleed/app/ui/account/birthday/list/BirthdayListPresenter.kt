package com.waleed.app.ui.account.birthday.list

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BirthdayListPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<BirthdayListView>() {
    fun getChildList() {

        if (isConnected) {
            view?.showLoading()
            apiDataSource.getChildList(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.list.isNullOrEmpty())
                            view?.showEmptyList()
                        else
                            view?.showList(it.list)
                    }else{
                        view?.showEmptyList()
                    }
                }, {
                    view?.hideLoading()
                })
        } else
            view?.showNoNetwork()
    }

    fun deleteChild(childID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.deleteChildItem(childID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        getChildList()
                    } else {
                        view?.showPop(it.message)
                    }
                }, { view?.hideLoading() })
        } else view?.showNoNetwork()
    }
}