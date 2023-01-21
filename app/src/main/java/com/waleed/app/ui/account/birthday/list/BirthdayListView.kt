package com.waleed.app.ui.account.birthday.list

import com.waleed.app.business.data.newdata.ChildListResponse
import com.waleed.app.ui.base.BaseView
import java.util.ArrayList

interface BirthdayListView:BaseView {
    fun showEmptyList()
    fun showList(list: ArrayList<ChildListResponse.ChildItem>)
}