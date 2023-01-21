package com.waleed.app.ui.account.edit

import android.content.Intent
import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class EditProfileActivity : BaseActivity(), EditProfileView {

    @Inject
    lateinit var presenter: EditProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        btn_close.setOnClickListener {
            onBackPressed()
        }
        btn_cart.makeGone()
        toolbar_page_title_tv.text = getString(R.string.edit_profile_string)

        et_name_title.setTextValue(LoggedUser.getInstance().getAccount()!!.customerName)
        et_email.setTextValue(LoggedUser.getInstance().getAccount()!!.emailID)
        et_mobile.setTextValue(LoggedUser.getInstance().getAccount()!!.mobile)
        et_email.isEnabled = false

        btn_update.setOnClickListener {
            presenter.validateData(et_name_title.getStringValue(), et_mobile.getStringValue())
        }
    }

    override fun emptyPhone() {
        showPop(getString(R.string.enter_valid_phone_number))
    }

    override fun emptyName() {
        showPop(getString(R.string.enter_valid_name_string))
    }

    override fun onSuccessUpdate() {
        Dialogs.alertDialog(
            this, getString(R.string.updated_profile_message), getString(R.string.oke_text)
        ) { _, _ ->
            setResult(RESULT_OK, Intent())
            finish()
        }
    }
}