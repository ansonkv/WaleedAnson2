package com.waleed.app.ui.orders.details

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.MyOrderResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.cart.CartActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.BUNDLE_ORDER_DETAILS
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.order_address_sheet.*
import kotlinx.android.synthetic.main.order_shop_address_sheet.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import javax.inject.Inject

class OrderDetailsActivity : BaseActivity(), OrderDetailsView {

    @Inject
    lateinit var presenter: OrderDetailsPresenter

    lateinit var orderDetails: MyOrderResponse.OrderItem
    private var isAddressLayoutVisible = false
    private var isShopAddressLayoutVisible = false
    private var isSheetVisible = false


    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        setContentView(R.layout.activity_order_details)

        initViews()
    }

    private fun initViews() {
        toolbar_page_title_tv.text = getText(R.string.order_details_string)
        btn_cart.makeGone()
        btn_close.setOnClickListener { onBackPressed() }
        btn_cart.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.goTOAnotherActivityWithBundle(this, CartActivity::class.java, bundle)
        }
        orderDetails =
            intent.getSerializableExtra(BUNDLE_ORDER_DETAILS) as MyOrderResponse.OrderItem

        tv_order_id_value.text = orderDetails.uniqueOrderID
        tv_transaction_type_value.text = orderDetails.paymentMode
        tv_transaction_id_value.text = orderDetails.transactionID
        tv_payment_status_value.text = orderDetails.paymentStatus

        tv_total_amount_value.text = orderDetails.totalAmt
        tv_delivery_amount_value.text = orderDetails.deliveryAmt
        if (orderDetails.wrapping.isNullOrEmpty()) {
            tv_wrapping_amount_value.text = getString(R.string.unit_price_string, "0.000")
        } else {
            var totalAmount = 0.0
            for (wrap in orderDetails.wrapping) {
                totalAmount += wrap.wrappingAmt.getAmount()
            }

            tv_wrapping_amount_value.text = getString(
                R.string.unit_price_string, String.format("%.3f", totalAmount)
            )
        }
        if (!orderDetails.couponCode.isNullOrEmpty()) {
            tv_wrapping_discount_title.makeVisible()
            tv_wrapping_discount_value.makeVisible()
            tv_wrapping_discount_title.text = getString(
                R.string.discount_code_value,
                orderDetails.couponCode
            )
            tv_wrapping_discount_value.text = orderDetails.discountAmt
        }
        tv_grand_amount_value.setLanguageString(orderDetails.netAmt, orderDetails.netAmtAr)
//        if (!orderDetails.deliveryAddress.isNullOrEmpty()) {
//            rl_address.makeVisible()
//            tv_address_value.text = orderDetails.deliveryAddress
//        }
        showListData(orderDetails.orderedProductItems)
        img_close_address.setOnClickListener { onAddressClick() }
        img_close_shop_address.setOnClickListener { onShopAddressClick() }
    }

    override fun showListData(listData: List<MyOrderResponse.OrderItem.OrderedProductItem>) {
        rv_product_list.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        var productAdapter = OrderDetailsProductAdapter(
            listData,
            this,
            orderDetails.orderStatusID == 3,
            orderDetails.orderDate,
            orderDetails.uniqueOrderID
        )
        rv_product_list.adapter = productAdapter
        productAdapter.onViewClick = {
            if (it.storeID == 0) {
                tv_address_name.text = orderDetails.deliveryAddress
                onAddressClick()
            } else {
                tv_store_address.text = it.storeAddress
                if (it.storeStatus == "Closed") {
                    tv_store_status.setLanguageString(
                        it.storeStatus, it.storeStatusAr
                    )
                    tv_store_status.setTextColor(resources.getColor(R.color.colorRed))
                    tv_store_open_time.text =
                        getString(R.string.will_open_string, it.closingTime)
                } else {

                    tv_store_status.setLanguageString(
                        it.storeStatus, it.storeStatusAr
                    )
                    tv_store_status.setTextColor(resources.getColor(R.color.colorGreen))
                    tv_store_open_time.text =
                        getString(R.string.will_close_string, it.closingTime)
                }
                onShopAddressClick()
            }
        }
    }

    private fun onAddressClick() {

        if (isAddressLayoutVisible) {
            isAddressLayoutVisible = false
            isSheetVisible = false
            layout_address.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_down
                )
            )
            layout_address.makeGone()
        } else {
            isAddressLayoutVisible = true
            isSheetVisible = true
            layout_address.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_up
                )
            )
            layout_address.makeVisible()
        }
    }

    private fun onShopAddressClick() {

        if (isShopAddressLayoutVisible) {
            isShopAddressLayoutVisible = false
            isSheetVisible = false
            layout_store_address.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_down
                )
            )
            layout_store_address.makeGone()
        } else {
            isShopAddressLayoutVisible = true
            isSheetVisible = true
            layout_store_address.startAnimation(
                AnimationUtils.loadAnimation(
                    this, R.anim.bottom_sheet_anim_up
                )
            )
            layout_store_address.makeVisible()
        }
    }

    override fun onBackPressed() {
        if (isSheetVisible) {
            if (isAddressLayoutVisible) {
                onAddressClick()
            } else {
                onShopAddressClick()
            }
        } else {
            super.onBackPressed()
        }

    }

}