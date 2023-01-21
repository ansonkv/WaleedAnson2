package com.waleed.app.ui.notification

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.NotificationData
import com.waleed.app.business.data.newdata.NotificationResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.orders.list.OrdersListActivity
import com.waleed.app.ui.productdetails.ProductDetailsActivity
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.makeGone
import com.waleed.app.util.makeVisible
import kotlinx.android.synthetic.main.activity_notification_list.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import javax.inject.Inject

class NotificationActivity : BaseActivity(), NotificationView {

    @Inject
    lateinit var presenter: NotificationPresenter
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_notification_list)

        initViews()

        presenter.getNotification()

    }

    private fun initViews() {
        toolbar_page_title_tv.text = getText(R.string.notificatoin_string)
        btn_cart.makeGone()
        btn_close.setOnClickListener { onBackPressed() }
    }

    override fun showNotificationList(list: ArrayList<NotificationResponse.NotificationItem>) {
        tvEmptyList.makeGone()
        rv_notification_list.makeVisible()
        rv_notification_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        notificationAdapter = NotificationAdapter(list, this)
        rv_notification_list.adapter = notificationAdapter
        notificationAdapter.onRAteClick = {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, OrdersListActivity::class.java, Bundle()
            )
        }
    }

    override fun showEmptyNotification() {
        tvEmptyList.makeVisible()
        rv_notification_list.makeGone()
    }
}