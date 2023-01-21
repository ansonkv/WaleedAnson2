package com.waleed.app.ui.base

import com.waleed.app.business.base.WKBaseBusiness
import com.waleed.app.util.ConnectivityReceiver
import java.lang.ref.WeakReference

abstract class BasePresenter<T: BaseView> {

    private var viewRef: WeakReference<T>? = null

    protected var baseBusiness : WKBaseBusiness? = null

    fun attachView(view: T) {
        viewRef = WeakReference(view)
    }

    protected val view: T?
        get() = if (viewRef == null) null else viewRef!!.get()

    protected val isViewAttached: Boolean
        get() = viewRef != null

    protected val isConnected: Boolean
        get() = ConnectivityReceiver.isConnected()

}