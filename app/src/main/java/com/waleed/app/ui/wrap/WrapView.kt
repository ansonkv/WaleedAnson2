package com.waleed.app.ui.wrap

import com.waleed.app.business.data.newdata.WrapperResponse
import com.waleed.app.ui.base.BaseView

interface WrapView:BaseView {

    fun showWrapperData(list: WrapperResponse)
    fun onSubmitSuccess()
}
