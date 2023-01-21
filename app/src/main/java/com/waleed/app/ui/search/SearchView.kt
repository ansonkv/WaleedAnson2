package com.waleed.app.ui.search

import com.waleed.app.business.data.newdata.SearchResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface SearchView:BaseView {
    fun noNetworkForSearch()
    fun showEmptySearch()
    fun showSearchResult(searchItemList: ArrayList<SearchResponse.SearchItem>)
}