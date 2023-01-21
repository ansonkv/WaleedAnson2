package com.waleed.app.ui.home.category

import android.annotation.SuppressLint
import android.util.Log
import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryPresenter @Inject constructor(private var apiDataSource: APIDataSource) : BasePresenter<CategoryView>() {

    @SuppressLint("CheckResult")
    fun getHomeSliderData() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.homeSliderData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.hideLoading()
                        view?.loadOfferSliderData(it.homeSliderDataList)
                    }else{
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                }, {
                    Log.e("Exception",it.toString())
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
    }
    fun getCategoryData() {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getCategoryList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status == 1) {
                        view?.hideLoading()
                        view?.showCategoryList(it.categoryList)
                    }else{
                        view?.hideLoading()
                        view?.showPop(it.message)
                    }
                },{
                    Log.e("Exception",it.toString())
                    view?.hideLoading()
                    view?.showErrorView()
                })
        } else {
            view?.showNoNetwork()
        }
//        var data = CategoryData(
//            1,
//            "Action Figures",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data1 = CategoryData(
//            2,
//            "Animals",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//        var data2 = CategoryData(
//            1,
//            "Action Figures",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data3 = CategoryData(
//            2,
//            "OutDoor",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//        var data4 = CategoryData(
//            1,
//            "Vehicles",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data5 = CategoryData(
//            2,
//            "Legos",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//        var data6 = CategoryData(
//            1,
//            "Action Figures",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data7 = CategoryData(
//            2,
//            "Animals",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//        var data8 = CategoryData(
//            1,
//            "Action Figures",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data9 = CategoryData(
//            2,
//            "Animals",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//        var data10 = CategoryData(
//            1,
//            "Action Figures",
//            "https://rukminim1.flixcart.com/image/832/832/k0lbdzk0/action-figure/y/2/q/cyberverse-action-attackers-1-step-changer-optimus-prime-action-original-imafj36wyycqhhjp.jpeg?q=70"
//        )
//        var data11 = CategoryData(
//            2,
//            "Animals",
//            "https://rukminim1.flixcart.com/image/832/832/jqwny4w0/stuffed-toy/6/t/b/dimpy-sitting-bear-hug-me-emb-40-dimpy-stuff-original-imafchsdhdsy7saj.jpeg?q=70"
//        )
//
//        var listData = listOf<CategoryData>(
//            data,
//            data1,
//            data2,
//            data3,
//            data4,
//            data5,
//            data6,
//            data7,
//            data8,
//            data9,
//            data10,
//            data11
//        )
//        view?.showCategoryList(listData)
    }
}