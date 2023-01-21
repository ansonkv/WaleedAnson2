package com.waleed.app.ui.home.profile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.ui.account.birthday.list.BirthdayListActivity
import com.waleed.app.ui.account.changepass.ChangePasswordActivity
import com.waleed.app.ui.account.contactus.ContactUsActivity
import com.waleed.app.ui.account.edit.EditProfileActivity
import com.waleed.app.ui.account.register.RegisterActivity
import com.waleed.app.ui.account.signin.SignInActivity
import com.waleed.app.ui.account.terms.TermsActivity
import com.waleed.app.ui.address.list.AddressListActivity
import com.waleed.app.ui.base.BaseFragment
import com.waleed.app.ui.favourite.FavouriteProductListActivity
import com.waleed.app.ui.home.FragmentClickListener
import com.waleed.app.ui.notification.NotificationActivity
import com.waleed.app.ui.orders.list.OrdersListActivity
import com.waleed.app.ui.splash.SplashActivity
import com.waleed.app.util.*
import com.waleed.app.util.AppConstants.ACCOUNT_UPDATE
import com.waleed.app.util.AppConstants.BUNDLE_TERMS_PAGE_NUMBER
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.lay_out_choose_lang.*
import kotlinx.android.synthetic.main.profile_fragment.*
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileVie {

    @Inject
    lateinit var presenter: ProfilePresenter
    private lateinit var fragmentClickListener: FragmentClickListener


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        showContent()
        fragmentClickListener.selectBottomBar(2)
        checkLogin()
    }

    private fun initAccountSetupView() {

        btn_login.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                context, SignInActivity::class.java,
                null, AppConstants.LOGIN_REQUEST_CODE
            )
        }
        btn_register.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                context, RegisterActivity::class.java,
                null, AppConstants.REGISTER_REQUEST_CODE
            )
        }

    }

    private fun initProfileView() {
        var bundle = Bundle()

        tv_user_name.text = LoggedUser.getInstance().getAccount()!!.customerName
        btn_myaddress.setOnClickListener {
            bundle.putBoolean(AppConstants.BUNDLE_FROM_HOME, true)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, AddressListActivity::class.java,
                bundle
            )
        }

        rl_fav.setOnClickListener {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, FavouriteProductListActivity::class.java, bundle
            )
        }

        rl_terms.setOnClickListener {
            bundle.putInt(BUNDLE_TERMS_PAGE_NUMBER, 1)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, TermsActivity::class.java, bundle
            )
        }

        rl_change_pass.setOnClickListener {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, ChangePasswordActivity::class.java, bundle
            )
        }

        rl_refund.setOnClickListener {
            bundle.putInt(BUNDLE_TERMS_PAGE_NUMBER, 3)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, TermsActivity::class.java, bundle
            )
        }

        rl_privacy.setOnClickListener {
            bundle.putInt(BUNDLE_TERMS_PAGE_NUMBER, 2)
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, TermsActivity::class.java, bundle
            )
        }
        btn_notification.setOnClickListener {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, NotificationActivity::class.java, bundle
            )
        }
        btn_my_order.setOnClickListener {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, OrdersListActivity::class.java, bundle
            )
        }
        rl_contact_us.setOnClickListener {
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context, ContactUsActivity::class.java, bundle
            )
        }
        btn_edit.setOnClickListener {
            ActivitiesManager.startActivityForResult(
                context, EditProfileActivity::class.java, bundle, ACCOUNT_UPDATE
            )
        }
        rl_language.setOnClickListener {
            showLanguageOption()
        }
        rl_log_out.setOnClickListener {
            Dialogs.alertDialogWithTwoButton(
                context!!,
                context!!.getString(R.string.log_out_message),
                context!!.getString(R.string.yes_string),
                { dialogInterface: DialogInterface, i: Int ->
                    LoggedUser.getInstance().logout()
                    checkLogin()
                },
                context!!.getString(R.string.no_string),
                null
            ).show()
        }
        rl_birthday.setOnClickListener {
            var bundle = Bundle()
            ActivitiesManager.goTOAnotherActivityWithBundle(
                context!!, BirthdayListActivity::class.java, bundle
            )
        }
    }

    override fun getMainLayout(): Int {
        return R.layout.fragment_profile
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentClickListener = context as FragmentClickListener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            checkLogin()
        }
        if (resultCode == ACCOUNT_UPDATE) {
            tv_user_name.text = LoggedUser.getInstance().getAccount()!!.customerName
        }
    }

    private fun checkLogin() {
        if (LoggedUser.getInstance().isUserLoggedIn()||Waleed.appContext.getAppPref()
                .getBoolean(AppConstants.PREF_GUEST_USER, false)) {
            ll_login.makeGone()
            ll_profile.makeVisible()
            initProfileView()
        } else {
            ll_login.makeVisible()
            ll_profile.makeGone()
            initAccountSetupView()
        }
    }

    private fun showLanguageOption() {
        val dialogue = Dialogs.createCustomDialog(
            activity,
            R.layout.lay_out_choose_lang,
            R.anim.slide_up,
            true,
            true,
            false
        )
        if (LangUtils.isCurrentLangArabic()) {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        } else {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.ll_arabic.setOnClickListener {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        }
        dialogue.ll_english.setOnClickListener {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.rb_eng.setOnClickListener {
            dialogue.rb_arabic.isChecked = false
            dialogue.rb_eng.isChecked = true
        }
        dialogue.rb_arabic.setOnClickListener {
            dialogue.rb_arabic.isChecked = true
            dialogue.rb_eng.isChecked = false
        }
        dialogue.btn_submit.setOnClickListener {
            Log.e("hnjn", "jhdkjh")
            if (dialogue.rb_arabic.isChecked) {
                Waleed.getInstance().getAppPref()
                    .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LangUtils.LANG_AR)
            } else {
                Waleed.getInstance().getAppPref()
                    .saveString(AppConstants.PREF_KEY_SELECTED_LANG, LangUtils.LANG_EN)
            }

            dialogue.dismiss()
            fragmentClickListener.onLanguageSelector()

        }
        dialogue.show()
    }
}