package com.waleed.app.business.data.newdata

data class SavedCart(var cartItemList: ArrayList<SavedCartItem> = arrayListOf()) {
    data class SavedCartItem(
        val itemId: Int = 0,
        var cartCount: Int = 0,
        var combinationId: Int = 0
    )
}