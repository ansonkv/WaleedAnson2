package com.waleed.app.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.waleed.app.R
import com.waleed.app.util.AppConstants
import com.waleed.app.util.Dialogs
import com.waleed.app.util.LangUtils


open class BaseActivity : AppCompatActivity(),
    BaseView {

    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        if (intent.getBooleanExtra(AppConstants.PREF_KEY_IS_SCREEN_ANIM_UP, false))

            overridePendingTransition(R.anim.slide_in_up, R.anim.no_change)
        else
            overridePendingTransition(R.anim.enter, R.anim.no_change)


        LangUtils.changeCurrentLocale(this, LangUtils.getCurrentAppLang())
    }

    fun initToolBar(
        toolbar: Toolbar,
        title: String,
        allowBack: Boolean,
        isSetCloseIcon: Boolean = false,
        isWhiteTheme: Boolean = false
    ) {

        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayShowTitleEnabled(false)

        /*  if (allowBack) {
              toolbar_left_btn.setOnClickListener {
                  finish()
              }

              if (isSetCloseIcon)
                  toolbar_left_btn.setImageResource(R.drawable.ic_close)
              else {

                  if (isWhiteTheme)

                      toolbar_left_btn.setImageResource(R.drawable.left_arrow_white)
                  else

                      toolbar_left_btn.setImageResource(R.drawable.left_arrow_black)

              }
          }

          toolbar_title.setText(title)

          if (isWhiteTheme)
              toolbar_title.setTextColor(Color.WHITE)
          else
              toolbar_title.setTextColor(Color.BLACK)
  */

    }

    fun initFragment(
        containerId: Int,
        fragmentName: String,
        bundle: Bundle?,
        allowAnim: Boolean = false
    ): Fragment {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Fragment.instantiate(this, fragmentName)

        if (bundle != null)
            fragment.arguments = bundle

        if (allowAnim)
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);


        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.commitAllowingStateLoss()

        return fragment
    }

    override fun showPop(message: String?) {
        Dialogs.alertDialog(this, message, getString(R.string.oke_text), null)
    }

    override fun showLoading() {
        if (loadingDialog == null)

            loadingDialog = Dialogs.createCustomDialog(
                this, R.layout.dialog_loading,
                0, true, false, false
            )

        loadingDialog!!.show()
    }

    override fun showLoading(context: Context) {

    }

    override fun hideLoading() {

        if (loadingDialog != null) {
            if (loadingDialog!!.isShowing) {
                loadingDialog!!.dismiss()
            }
        }
    }

    override fun showLoaderDialog() {
    }

    override fun hideLoaderDialog() {
    }

    override fun showErrorView() {
    }

    override fun showContent() {
    }

    override fun showEmpty() {
    }

    override fun onRetry() {
    }

    override fun showNoNetwork() {
        showPop(getString(R.string.no_network_message))
    }
}