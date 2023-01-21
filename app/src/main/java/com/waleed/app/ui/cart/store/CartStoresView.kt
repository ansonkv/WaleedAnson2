package com.waleed.app.ui.cart.store

import com.waleed.app.business.data.StoreData
import com.waleed.app.business.data.newdata.ProductStore
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface CartStoresView : BaseView {
    fun showCartStoreList(productStores: ArrayList<ProductStore>)
    fun showEmptyList()
    fun onStoreUpdatedSuccessfully()
}