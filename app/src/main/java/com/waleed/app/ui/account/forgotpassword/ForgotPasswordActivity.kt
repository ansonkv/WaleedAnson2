package com.waleed.app.ui.account.forgotpassword

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ForgotPassword
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.Dialogs
import com.waleed.app.util.StringUtils
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.et_email
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class ForgotPasswordActivity : BaseActivity(), ForgotPasswordView {
    @Inject
    lateinit var presenter: ForgotPasswordPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        btn_cart.makeGone()
        btn_language.makeGone()
        btn_submit.setOnClickListener {
            if (validateData()) {
                presenter.forgotPass(
                    et_email.text.toString().trim()
                )
            }
        }

    }

    private fun validateData(): Boolean {
        et_email.error = null
        et_email.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        return if (!StringUtils.isEmailValid(et_email.text.toString())) {
            et_email.setBackgroundResource(R.drawable.bg_edit_text_error_round)
            et_email.error = getString(R.string.enter_valid_email_string)
            et_email.requestFocus()
            false
        } else {
            true
        }
    }

    override fun onSubmitEmail(it: ForgotPassword?) {
        Dialogs.alertDialog(
            this,
            it!!.message,
            getString(R.string.oke_text),
            DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            })
    }
}