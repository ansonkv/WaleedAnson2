package com.waleed.app.ui.orders.list

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OredersListPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<OrdersListView>() {
    fun getOrderListData() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getMyOrderList(
                customerID = LoggedUser.getInstance().getAccount()!!.customerID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.orderItems.isNullOrEmpty()) {
                            view?.showEmptyList()
                        } else {
                            view?.showOrderList(it.orderItems)
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }, {

                })
        } else view?.showNoNetwork()

    }
}