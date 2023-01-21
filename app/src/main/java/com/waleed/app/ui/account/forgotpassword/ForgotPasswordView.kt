package com.waleed.app.ui.account.forgotpassword

import com.waleed.app.business.data.newdata.ForgotPassword
import com.waleed.app.ui.base.BaseView

interface ForgotPasswordView:BaseView {
    fun onSubmitEmail(it: ForgotPassword?)
}