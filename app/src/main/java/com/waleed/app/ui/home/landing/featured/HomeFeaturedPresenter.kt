package com.waleed.app.ui.home.landing.featured

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeFeaturedPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<HomeFeaturedView>() {

    fun getFeaturedProducts() {
        if (isConnected) {
            view?.onFeaturedLoading()
//            view?.showLoaderInList()
            apiDataSource.getFeaturedProductList(
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
//                    view?.hideLoaderInList()
                    view?.onFeaturedLoadingFinished()
                    if (it.status == 1) {
                        view?.showFeaturedProductLists(it.featureList)
                    } else {
                        view?.showEmptyList()
                    }

                }, {
                    view?.showEmptyList()
                })
        }

    }

    fun addToFavourite(data: ProductItemData) {
        if (isConnected) {

            apiDataSource.addFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = data.productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavAddedSuccess(data)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun removeFromFavourite(data: ProductItemData) {
        if (isConnected) {
            apiDataSource.removeFavouriteItem(
                customerId = LoggedUser.getInstance().getAccount()!!.customerID,
                productId = data.productID
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.onFavRemoveSuccess(data)
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
        data: ProductItemData
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
                        getCartListData(data)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    private fun getCartListData( productItemData: ProductItemData) {
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
                        view?.onCartAddedSuccessfully(productItemData)
                    } else {
                        Waleed.getInstance().getAppPref().removeSharedPref(AppConstants.PREF_LOCAL_CART)
                    }
                }, {})

        } else {
            view?.showNoNetwork()
        }
    }
}