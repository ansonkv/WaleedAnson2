package com.waleed.app.ui.search

import com.waleed.app.business.network.APIDataSource
import com.waleed.app.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchPresenter @Inject constructor(private var apiDataSource: APIDataSource) :
    BasePresenter<SearchView>() {
    fun searchItem(searchString: String) {
        if (isConnected) {
            apiDataSource.getSearchResult(searchString).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status==1){
                        view?.showSearchResult(it.searchItemList)
                    }else{
                        view?.showEmptySearch()
                    }
                }, {})
        } else {
            view?.showNoNetwork()
            view?.noNetworkForSearch()
        }
    }
}