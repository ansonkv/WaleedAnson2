package com.waleed.app.ui.wrap

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WrapPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<WrapView>() {


    fun getWrapperPaperData() {
        view?.showLoading()
        apiDataSource.getWrapperData().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.hideLoading()
                if (it.status == 1) {
                    view?.showWrapperData(it)
                } else {
                }
            }, {})
    }

    fun submitWrapper(
        selectedCartIdString: String,
        selectedTagId: Int,
        selectedPaperId: Int,
        selectedMessage: String
    ) {
        if (isConnected) {
            view?.showLoading()
            apiDataSource.submitWrapper(
                selectedCartIdString, selectedTagId, selectedPaperId, selectedMessage
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view?.hideLoading()
                    if (it.status==1)
                        view?.onSubmitSuccess()
                    else
                        view?.showPop(it.message)
                },{})
        } else view?.showNoNetwork()
    }
}