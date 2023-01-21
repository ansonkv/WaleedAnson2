package com.waleed.app.ui.cart.store


import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartStorePresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<CartStoresView>() {

    fun getStoreData(cartID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getStoreListForCartItem()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showCartStoreList(it.storeList)
                    } else {
                        view?.showEmptyList()
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }

    }

    fun updateStoreCart(cartID: Int, storeID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.updateShopForCartItem(
                cartID = cartID, storeId = storeID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onStoreUpdatedSuccessfully()
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }
}