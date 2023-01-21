package com.waleed.app.ui.account.changepass

import android.os.Bundle
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.ChangePasswordResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.makeGone
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class ChangePasswordActivity : BaseActivity(), ChangePassView {

    @Inject
    lateinit var presenter: ChangePassPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        btn_language.makeGone()
        btn_cart.makeGone()
        btn_save.setOnClickListener {
            if (validateData()) {
                presenter.updatePassword(
                    et_current_password.text.toString().trim(),
                    et_new_password.text.toString().trim(),
                    et_conf_new_password.text.toString().trim()
                )
            }
        }
    }

    private fun validateData(): Boolean {
        et_current_password.error = null
        et_new_password.error = null
        et_conf_new_password.error = null
        et_current_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_new_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_conf_new_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        return when {
            et_current_password.text.toString().isEmpty() -> {
                et_current_password.error = getString(R.string.enter_current_password)
                et_current_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_current_password.requestFocus()
                false
            }
            et_new_password.text.toString().isEmpty() -> {
                et_new_password.error = getString(R.string.enter_your_new_password)
                et_new_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_new_password.requestFocus()
                false
            }
            et_conf_new_password.text.toString().isEmpty() -> {
                et_conf_new_password.error = getString(R.string.new_password_should_match)
                et_conf_new_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_conf_new_password.requestFocus()
                false
            }
            et_conf_new_password.text.toString() != et_new_password.text.toString() -> {
                et_conf_new_password.error = getString(R.string.new_password_should_match)
                et_conf_new_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_conf_new_password.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onSuccessPasswordChange(it: ChangePasswordResponse) {
        showPop(it.message)
    }

    override fun onFailedPasswordChange(changePasswordResponse: ChangePasswordResponse?) {
        showPop(changePasswordResponse!!.message)
        if (changePasswordResponse.status == 3) {
            et_current_password.error = changePasswordResponse!!.message
            et_current_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
            et_current_password.requestFocus()
        }
    }
}