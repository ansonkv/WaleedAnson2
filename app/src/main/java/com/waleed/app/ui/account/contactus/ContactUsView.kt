package com.waleed.app.ui.account.contactus

import com.waleed.app.business.data.newdata.SocialMediaReponse
import com.waleed.app.ui.base.BaseView

interface ContactUsView : BaseView {
    fun onSuccessSubmit()
    fun setSocialLinks(links: SocialMediaReponse)

}