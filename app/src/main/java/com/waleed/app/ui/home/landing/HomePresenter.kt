package com.waleed.app.ui.home.landing

import android.annotation.SuppressLint
import android.util.Log
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomePresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<HomeView>() {
    @SuppressLint("CheckResult")
    fun getHomeSliderData() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.homeSliderData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.loadOfferSliderData(it.homeSliderDataList)
                        getIdealCategory()
                    } else {
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                }, {
                    Log.e("Exception", it.toString())
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }

    @SuppressLint("CheckResult")
    fun getIdealCategory() {
        if (isConnected) {
            apiDataSource.getIdealCategoryList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        view?.showIdealCategoryList(it.idealTypeList)
                    } else {
                        view?.showPop(it.message)
                    }
                }, {
                    Log.e("Exception", it.toString())
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }
}