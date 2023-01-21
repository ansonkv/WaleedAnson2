package com.waleed.app.ui.productdetails

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.AppConstants.PREF_LOCAL_CART
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProductDetailsPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<ProductDetailsView>() {

    fun getProductDetails(productId: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getProductDetails(
                productId = productId,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {

                        view?.showProductDetails(it)
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
        isFromList: Boolean,
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

                        getCartListData(isFromList, productItemData)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    private fun getCartListData(isFromList: Boolean, productItemData: ProductItemData) {
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
                        Waleed.getInstance().getAppPref().saveObject(PREF_LOCAL_CART, it)
                        view?.onCartItemAddSuccess(it.cartItems.size, isFromList, productItemData)
                    } else {
                        Waleed.getInstance().getAppPref().removeSharedPref(PREF_LOCAL_CART)
                    }
                }, {})

        } else {
            view?.showNoNetwork()
        }
    }

    fun getCartCount(isFromList: Boolean, productItemData: ProductItemData) {
        if (isConnected) {
            view?.showLoading()
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
                    view?.hideLoading()
                    if (it.status != 0) {


                    } else {

                    }
                }, {

                })
        }
    }

    fun addToFavourite(productID: Int) {
        if (isConnected) {

            apiDataSource.addFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavAddedSuccess()
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun addToFavourite(data: ProductItemData, position: Int) {
        if (isConnected) {

            apiDataSource.addFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = data.productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavAddedSuccess(data, position)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun removeFromFavourite(productID: Int) {
        if (isConnected) {
            apiDataSource.removeFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavRemoveSuccess()
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
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

}