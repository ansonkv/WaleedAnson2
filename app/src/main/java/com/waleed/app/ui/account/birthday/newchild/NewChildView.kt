package com.waleed.app.ui.account.birthday.newchild

import com.waleed.app.business.data.newdata.BaseResponse
import com.waleed.app.ui.base.BaseView

interface NewChildView:BaseView {
    fun addedSuccessfully(baseResponse: BaseResponse)
}