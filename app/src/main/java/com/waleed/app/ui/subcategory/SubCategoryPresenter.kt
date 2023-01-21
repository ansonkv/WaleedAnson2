package com.waleed.app.ui.subcategory

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SubCategoryPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<SubCategoryView>() {
    fun getSubCategoryList(categoryId: Int) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.getSubCategoryList(categoryId = categoryId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status == 1) {
                        if (it.subCategoryList.isNotEmpty()) {
                            view?.showSubCategoryList(it.subCategoryList)
                        } else {
                            view?.showEmptyList()
                        }
                    } else {
                        view?.showEmptyList()
                    }
                }, {
                    view?.showEmptyList()
                })
        } else {
            view?.showNoNetwork()
        }
    }
}