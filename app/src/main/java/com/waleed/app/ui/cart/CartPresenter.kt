package com.waleed.app.ui.cart

import com.waleed.app.Waleed
import com.waleed.app.business.data.CartData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CartPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<CartView>() {


    fun getCartListData() {
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
                        view?.showCartListData(it)
                        Waleed.getInstance().getAppPref().saveObject(AppConstants.PREF_LOCAL_CART, it)

                    } else {
                        view?.showEmptyList()
                    }
                }, {})

        } else {
            view?.showNoNetwork()
        }
    }

    fun removeCartItem(cartId: Int, productID: Int, combinationID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.removeCartItem(
                deviceId = Waleed.getInstance().getAppPref()
                    .getString(AppConstants.PREF_DEVICE_TOKEN)!!,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                },
                cartID = cartId
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        getCartListData()
                        Waleed.getInstance().getAppPref().saveObject(AppConstants.PREF_LOCAL_CART, it)
                        view?.itemRemovedSuccessfully(productID, combinationID)
                    } else {
                        view?.hideLoading()
                        view?.removeCartFailed()
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

    fun updateCartItemCount(quantity: Int, cartId: Int, combinationID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.updateCartItemCount(
                quantity = quantity,
                cartID = cartId,
                combinationId = combinationID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        getCartListData()
                    } else {
                        view?.showPop(it.message)
                    }
                }, {

                })
        } else {
            view?.showNoNetwork()
        }
    }

    fun wrapperRemove(cartID: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.submitWrapper(
                cartID.toString(), 0, 0, ""
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status==1)
                        view?.onWrapperRemove()
                    else
                        view?.showPop(it.message)
                },{})
        } else view?.showNoNetwork()
    }

}