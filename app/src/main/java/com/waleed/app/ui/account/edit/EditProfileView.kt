package com.waleed.app.ui.account.edit

import com.waleed.app.ui.base.BaseView

interface EditProfileView:BaseView {
    fun emptyPhone()
    fun emptyName()
    fun onSuccessUpdate()
}