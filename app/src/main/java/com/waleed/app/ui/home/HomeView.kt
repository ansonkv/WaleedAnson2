package com.waleed.app.ui.home

import com.waleed.app.ui.base.BaseView

interface HomeView:BaseView {
    fun onCartItemAddSuccess(cartCount: Int)
}