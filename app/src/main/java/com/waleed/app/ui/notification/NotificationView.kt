package com.waleed.app.ui.notification

import com.waleed.app.business.data.newdata.NotificationResponse
import com.waleed.app.ui.base.BaseView

interface NotificationView:BaseView {
    fun showNotificationList(list: ArrayList<NotificationResponse.NotificationItem>)
    fun showEmptyNotification()
}