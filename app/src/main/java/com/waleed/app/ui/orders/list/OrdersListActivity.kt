package com.waleed.app.ui.orders.list

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.orders.details.OrderDetailsActivity
import com.waleed.app.ui.orders.list.adapters.OrderListAdapter
import com.waleed.app.util.ActivitiesManager
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_DETAILS
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.activity_order_list.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import javax.inject.Inject

class OrdersListActivity : BaseActivity(),
    OrdersListView {

    @Inject
    lateinit var presenter: OredersListPresenter

    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_order_list)

        initViews()

        presenter.getOrderListData()
    }

    private fun initViews() {
        toolbar_page_title_tv.text = getText(R.string.my_order_string)
        btn_cart.makeGone()
        btn_close.setOnClickListener { onBackPressed() }
    }

    override fun showOrderList(listOf: ArrayList<MyOrderResponse.OrderItem>) {
        rv_order_list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        orderListAdapter = OrderListAdapter(listOf, this)
        rv_order_list.adapter = orderListAdapter

        orderListAdapter.onItemClick = {
            var bundle = Bundle()
            bundle.putSerializable(BUNDLE_ORDER_DETAILS,it)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, OrderDetailsActivity::class.java, bundle
            )
        }

    }

    override fun showEmptyList() {

    }
}