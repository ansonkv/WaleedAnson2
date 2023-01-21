package com.waleed.app.ui.account.register

import com.waleed.app.business.data.newdata.RegisterResponse
import com.waleed.app.ui.base.BaseView

interface SignUpView:BaseView {
    fun onSuccessLogin(response: RegisterResponse)
    fun onExistingEmailId()
}