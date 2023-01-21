package com.waleed.app.ui.account.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.waleed.app.R
import com.waleed.app.Waleed
import com.waleed.app.business.data.newdata.SocialMediaReponse
import com.waleed.app.ui.base.BaseActivity
import com.waleed.app.util.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.toolbar_product_details.*
import javax.inject.Inject


class ContactUsActivity : BaseActivity(), ContactUsView {
    @Inject
    lateinit var presenter: ContactUsPresenter

    private var reportList: ArrayList<String> = arrayListOf()
    private var selectedReportType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        Waleed.getAppComponent().inject(this)
        presenter.attachView(this)

        reportList.addAll(
            arrayListOf(
                getString(R.string.advice_string),
                getString(R.string.issue_string)
            )
        )

        var reportTypeAdapter = ContactUsSpinnerAdapter(this, reportList)
        spinner_reason.adapter = reportTypeAdapter
        spinner_reason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> selectedReportType = 1
                    1 -> selectedReportType = 2
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        presenter.getSocialMediaLinks()
        toolbar_page_title_tv.text = getString(R.string.contact_us)
        btn_close.setOnClickListener {
            onBackPressed()
        }
        btn_cart.makeGone()

        et_email.setTextValue(LoggedUser.getInstance().getAccount()!!.emailID)
        et_email.isEnabled = false
        btn_submit.setOnClickListener {
            if (validateData()) {
                presenter.submitContactUsForm(
                    name = et_name.getStringValue(),
                    reportType = selectedReportType,
                    message = et_message.getStringValue(),
                    email = et_email.getStringValue(),
                    phone = et_mobile.getStringValue()
                )
            }
        }


    }

    private fun validateData(): Boolean {
        et_name.error = null
        et_mobile.error = null
        et_message.error = null
        et_email.error = null
        return when {
            et_name.getStringValue().isNullOrEmpty() -> {
                et_name.error = getString(R.string.enter_valid_name_string)
                et_name.requestFocus()
                false
            }
            !StringUtils.isEmailValid(et_email.getStringValue()) -> {
                et_email.error = getString(R.string.enter_valid_email_string)
                et_email.requestFocus()
                false
            }
            et_mobile.getStringValue().isNullOrEmpty() -> {
                et_mobile.error = getString(R.string.enter_valid_phone_number)
                et_mobile.requestFocus()
                false
            }
            et_message.getStringValue().isNullOrEmpty() -> {
                et_message.error = getString(R.string.enter_valid_message)
                et_message.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onSuccessSubmit() {
        Dialogs.alertDialog(
            this, getString(R.string.equiry_submited_success_string), getString(R.string.oke_text)
        ) { _, _ -> onBackPressed() }
    }

    override fun setSocialLinks(response: SocialMediaReponse) {
        btn_social_facebook.setOnClickListener {
            openBrowser(response.links.facebookLink)
        }
        btn_social_instagram.setOnClickListener {
            openBrowser(response.links.instagramLink)
        }
        btn_social_twitter.setOnClickListener {
            openBrowser(response.links.twitterLink)
        }
        tvCallUs.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" +response.contactInfo.telephone )
            startActivity(callIntent)
        }
    }

    fun openBrowser(url: String) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}