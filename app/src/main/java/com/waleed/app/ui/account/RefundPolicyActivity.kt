package com.waleed.app.ui.account

import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_home.*

class RefundPolicyActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        toolbar_page_title_tv.text = getString(R.string.refund_policy_string)
        btn_close.setOnClickListener {
            onBackPressed()
        }
        btn_close.setOnClickListener { onBackPressed() }
    }
}