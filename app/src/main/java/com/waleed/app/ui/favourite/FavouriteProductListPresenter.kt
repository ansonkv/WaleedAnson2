package com.waleed.app.ui.favourite

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FavouriteProductListPresenter @Inject constructor(private var apiDataSource: APIDataSource)
    :BasePresenter<FavouriteProductListView>(){
    fun getFavouriteProducts() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getFavouriteProducts(
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showProductList(it.list)
                    }
                }, {
                    view?.showEmptyList()
                })
        } else {
            view?.hideLoading()
        }
    }

    fun removeFromFavourite(data: ProductItemData, position: Int) {
        if (isConnected) {
            apiDataSource.removeFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = data.productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavRemoveSuccess(data, position)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun addToCart(
        productId: Int,
        combinationId: Int,
        quantity: Int,
        storeId: Int,
        productItemData: ProductItemData
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.addToCart(
                productId = productId,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                },
                combinationId = combinationId,
                deviceToken = Waleed.getInstance().getAppPref()
                    .getString(AppConstants.PREF_DEVICE_TOKEN)!!,
                quantity = quantity,
                storeId = storeId
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status != 0) {
                        view?.onCartAddedSuccessfully(productItemData)
                        getCartListData()
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }
    private fun getCartListData() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getCartListData(
                deviceId = Waleed.getInstance().getAppPref()
                    .getString(AppConstants.PREF_DEVICE_TOKEN)!!,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        Waleed.getInstance().getAppPref().saveObject(AppConstants.PREF_LOCAL_CART, it)
                        view?.onCartItemAddSuccess(it.cartItems.size)
                    } else {
                        Waleed.getInstance().getAppPref().removeSharedPref(AppConstants.PREF_LOCAL_CART)
                    }
                }, {})

        } else {
            view?.showNoNetwork()
        }
    }
}