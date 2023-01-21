package com.waleed.app.ui.account.terms

import com.waleed.app.business.data.newdata.PrivacyPolicyResponse
import com.waleed.app.business.data.newdata.RefundPolicyResponse
import com.waleed.app.business.data.newdata.TermsConditionsResponse
import com.waleed.app.ui.base.BaseView

interface TermsView :BaseView {
    fun displayTermsAndConditions(termsConditions: TermsConditionsResponse.TermsConditions)
    fun displayPrivacyPolicy(privacyPolicy: PrivacyPolicyResponse.PrivacyPolicy)
    fun displayRefundPolicy(refundPolicy: RefundPolicyResponse.RefundPolicy)
}