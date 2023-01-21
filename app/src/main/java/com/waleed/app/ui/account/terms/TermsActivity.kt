package com.waleed.app.ui.account.terms

import android.os.Build
import android.os.Bundle
import android.text.Html
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.PrivacyPolicyResponse
import com.waleed.app.business.data.newdata.RefundPolicyResponse
import com.waleed.app.business.data.newdata.TermsConditionsResponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.AppConstants
import com.waleed.app.util.LangUtils
import com.waleed.app.util.makeGone
import com.waleed.app.util.setLanguageString
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.android.synthetic.main.product_details_activity.*
import kotlinx.android.synthetic.main.toolbar_home.*
import javax.inject.Inject

class TermsActivity : BaseActivity(), TermsView {
    @Inject
    lateinit var presenter: TermsPresenter

    private var pageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)
        btn_cart.makeGone()
        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)
        pageNumber = intent.getIntExtra(AppConstants.BUNDLE_TERMS_PAGE_NUMBER, 0)
        when (pageNumber) {
            1 -> {
                toolbar_page_title_tv.text = getString(R.string.terms_and_condition_string)
                presenter.getTermsAndCondition()
            }
            2 -> {
                toolbar_page_title_tv.text = getString(R.string.privacy_policy_string)
                presenter.getPrivacyPolicy()
            }
            3 -> {
                toolbar_page_title_tv.text = getString(R.string.refund_policy_string)
                presenter.getRefund()
            }
        }

        btn_close.setOnClickListener {
            onBackPressed()
        }
           }

    override fun displayTermsAndConditions(termsConditions: TermsConditionsResponse.TermsConditions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_terms.text =
                Html.fromHtml(
                    LangUtils.getLanguageString(
                        termsConditions.description, termsConditions.descriptionAr
                    ), Html.FROM_HTML_MODE_COMPACT
                )
        } else {
            tv_terms.text = Html.fromHtml(
                LangUtils.getLanguageString(
                    termsConditions.description, termsConditions.descriptionAr
                )
            )
        }

    }

    override fun displayPrivacyPolicy(privacyPolicy: PrivacyPolicyResponse.PrivacyPolicy) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_terms.text =
                Html.fromHtml(
                    LangUtils.getLanguageString(
                        privacyPolicy.description, privacyPolicy.descriptionAr
                    ), Html.FROM_HTML_MODE_COMPACT
                )
        } else {
            tv_terms.text = Html.fromHtml(
                LangUtils.getLanguageString(
                    privacyPolicy.description, privacyPolicy.descriptionAr
                )
            )
        }

    }

    override fun displayRefundPolicy(refundPolicy: RefundPolicyResponse.RefundPolicy) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_terms.text =
                Html.fromHtml(
                    LangUtils.getLanguageString(
                        refundPolicy.description, refundPolicy.descriptionAr
                    ), Html.FROM_HTML_MODE_COMPACT
                )
        } else {
            tv_terms.text = Html.fromHtml(
                LangUtils.getLanguageString(
                    refundPolicy.description, refundPolicy.descriptionAr
                )
            )
        }
    }
}