package com.waleed.app.business.data

data class StoreData(
    var storeId: Int,
    var storeName: String,
    var storeAddress: String,
    var isStoreOpen: Boolean,
    var time: String,
    var isStoreSelected:Boolean
)