package com.waleed.app.ui.account.signin

import com.waleed.app.ui.base.BaseView

interface SignInView:BaseView {
    fun onSuccessLogin()
    fun onInvalidCredentials()
}