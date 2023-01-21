package com.waleed.app.ui.stores

import com.waleed.app.business.data.StoreData
import com.waleed.app.ui.base.BaseView

interface StoresView:BaseView {
    fun showStoreList(listData: List<StoreData>)
}