package com.waleed.app.ui.account.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.RegisterResponse
import com.waleed.app.ui.account.terms.TermsActivity
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.ui.home.landing.HomeFragment
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class RegisterActivity : BaseActivity(), SignUpView {

    enum class LoginType(val value: Int) {
        WEB(1),
        FACEBOOK(2),
        GOOGLE(3)
    }

    @Inject
    lateinit var presenter: RegisterPresenter
    private var extrass = Bundle()
    var isRBChecked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        setContentView(R.layout.activity_register)

        initViews()
    }

    private fun initViews() {
        btn_language.makeGone()
        btn_cart.makeGone()
        rb_terms.setOnCheckedChangeListener { buttonView, isChecked ->
            rb_terms.isChecked = !isRBChecked
            isRBChecked = !isRBChecked
        }
        tv_terms.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt(AppConstants.BUNDLE_TERMS_PAGE_NUMBER, 1)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                this, TermsActivity::class.java, bundle
            )
        }
        btn_submit.setOnClickListener {
            if (validateData()) {
                Log.e("Validation Success", ".slafrw")
                presenter.registerAccount(
                    et_user_name.text.toString(),
                    et_email.text.toString(),
                    et_password.text.toString(),
                    et_confirm_password.text.toString(),
                    et_mobile.text.toString(),
                    "965", LoginType.WEB.value
                )
            }


//            Waleed.getInstance().getAppPref().saveBoolean(AppConstants.PREF_IS_LOGGED_IN, true)
//            var intent = Intent()
//            intent.putExtra(AppConstants.BUNDLE_LOGGED_USER_ID, 123)
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }
        btn_cart.makeGone()
        btn_search.makeGone()
    }

    private fun validateData(): Boolean {
        et_user_name.error = null
        et_email.error = null
        et_password.error = null
        et_confirm_password.error = null
        et_mobile.error = null
        et_user_name.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_email.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_confirm_password.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        et_mobile.setBackgroundResource(R.drawable.bg_edit_text_white_round)
        return if (isRBChecked) {
            if (et_user_name.text.toString().isEmpty()) {
                et_user_name.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_user_name.error = getString(R.string.enter_valid_name_string)
                et_user_name.requestFocus()
                return false
            } else if (!StringUtils.isEmailValid(et_email.text.toString())) {
                et_email.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_email.error = getString(R.string.enter_valid_email_string)
                et_email.requestFocus()
                return false
            } else if (et_password.text.toString().isEmpty()) {
                et_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_password.error = getString(R.string.enter_valid_password_string)
                et_password.requestFocus()
                return false
            } else if (et_confirm_password.text.toString() != et_password.text.toString()) {
                et_confirm_password.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_confirm_password.error = getString(R.string.enter_valid_password_string)
                et_confirm_password.requestFocus()
                return false
            } else if (et_mobile.text.toString().isEmpty()) {
                et_mobile.setBackgroundResource(R.drawable.bg_edit_text_error_round)
                et_mobile.error = getString(R.string.enter_valid_phone_number)
                et_mobile.requestFocus()
                return false
            } else {
                return true
            }
        } else {
            showPop(getString(R.string.please_accept_terms))
            false
        }
    }

    override fun onSuccessLogin(response: RegisterResponse) {
        Dialogs.alertDialog(
            this,
            response!!.message,
            getString(R.string.oke_text)
        ) { _, _ ->
            if (extrass.getInt(AppConstants.BUNDLE_FROM_PAGE, 0) == 1) {
//                val bundle = Bundle()
//                ActivitiesManager.startHomeActivity(
//                    this,
//                    HomeFragment::class.java.name,
//                    bundle,
//                    false
//                )
//                finish()
            } else {
                var intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onExistingEmailId() {
        showPop(getString(R.string.existing_email_string))
        et_email.requestFocus()
    }
}