package com.waleed.app.ui.address.list

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddressListPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<AddressListView>() {

    fun getAddressList() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getAddressList(
                LoggedUser.getInstance().getAccount()!!.customerID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.addressItemList.isNullOrEmpty()) {
                            view?.showEmptyList()
                        } else {
                            view?.showAddressList(it.addressItemList)
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }

    }

    fun deleteAddress(customerAddressID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.deleteAddress(customerAddressID,
                LoggedUser.getInstance().getAccount()!!.customerID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.onSuccessDelete(it.message)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }
}