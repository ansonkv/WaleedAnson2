package com.waleed.app.ui.home.landing.ideal

import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ProductItemData
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LoggedUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeIdealPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<HomeIdealView>() {

    fun getAgeGroupList(idealID: Int) {
        if (isConnected) {

            apiDataSource.getAgeGroupList(
                idealId = idealID
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    if (it.status == 1) {
                        view?.viewAgeGroupList(it.ageGroupList)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {})
        } else {
            view?.showNoNetwork()
        }
    }

    fun getAllProductList(idealID: Int) {
        if (isConnected) {
            view?.onProductStartLoading()
            apiDataSource.getAllIdealProductList(
                idealId = idealID,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    view?.onProductLoadingFinished()
                    if (it.status == 1) {
                        if (it.productItem.isNotEmpty()) {
                            view?.showIdealProductList(it.productItem)
                        } else {
                            view?.showEmptyList()
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }
        } else {
            view?.showNoNetwork()
        }
    }

    fun getAgeGroupData(ageGroupID: Int) {
        if (isConnected) {
            view?.onProductStartLoading()
            apiDataSource.getProductListOnAgeGroup(
                ageGroupId = ageGroupID,
                customerID = if (LoggedUser.getInstance().isUserLoggedIn()) {
                    LoggedUser.getInstance().getAccount()!!.customerID
                } else {
                    0
                }
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.onProductLoadingFinished()
                    if (it.status == 1) {
                        if (it.productItem.isNotEmpty()) {
                            view?.showIdealProductList(it.productItem)
                        } else {
                            view?.showEmptyList()
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }, {

                })
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
                        getCartListData(productItemData)
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