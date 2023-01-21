package com.waleed.app.ui.payment

import android.content.Intent
import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.CheckOutResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.home.HomeActivity
import com.waleed.app.ui.home.HomeActivity.Companion.FRAGMENT_NAME_TAG
import com.waleed.app.ui.home.HomeActivity.Companion.OPEN_MY_ORDER
import com.waleed.app.ui.home.landing.HomeFragment
import com.waleed.app.ui.home.profile.ProfileFragment
import com.waleed.app.util.AppConstants
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.activity_sucess_purchase.*
import kotlinx.android.synthetic.main.toolbar_home.*

class PaymentSuccess : BaseActivity() {
    private lateinit var checkOutInfo: CheckOutResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        setContentView(R.layout.activity_sucess_purchase)
        checkOutInfo = intent.getSerializableExtra(AppConstants.BUNDLE_PAYMENT_DETAILS)
                as CheckOutResponse
        btn_search.makeGone()
        btn_cart.makeGone()
        btn_language.makeGone()
        tv_order_id_value.text = checkOutInfo.orderID.toString()
        tv_transaction_value.text = checkOutInfo.paymentMethodName
        tv_transaction_id_value.text = checkOutInfo.transactionID
        btn_my_order.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            var bundle = Bundle()
            bundle.putBoolean(OPEN_MY_ORDER, true)
            bundle.putString(FRAGMENT_NAME_TAG, ProfileFragment::class.java.name)
            i.putExtras(bundle)
            startActivity(i)
        }
        btn_home.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            var bundle = Bundle()
            bundle.putString(FRAGMENT_NAME_TAG, HomeFragment::class.java.name)
            i.putExtras(bundle)
            startActivity(i)
        }
    }
}