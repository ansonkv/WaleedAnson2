package com.waleed.app.ui.base

import android.content.Context


interface BaseView {

    fun showPop(message: String?)

    fun showLoading()

    fun hideLoading()

    fun showLoaderDialog()

    fun hideLoaderDialog()

    fun showErrorView()

    fun showContent()

    fun showEmpty()

    fun onRetry()

    fun showNoNetwork()

    fun showLoading(context: Context)
}