package com.waleed.app.ui.account.changepass

import com.waleed.app.business.data.newdata.ChangePasswordResponse
import com.waleed.app.ui.base.BaseView

interface ChangePassView:BaseView {
    fun onSuccessPasswordChange(it: ChangePasswordResponse)
    fun onFailedPasswordChange(changePasswordResponse: ChangePasswordResponse?)
}