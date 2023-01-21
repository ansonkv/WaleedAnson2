package com.waleed.app.ui.splash

import com.waleed.app.Waleed
import com.waleed.app.ui.base.BasePresenter
import com.waleed.app.util.AppConstants
import javax.inject.Inject

class SplashPresenter @Inject constructor() : BasePresenter<SplashView>() {

    @Inject
    fun provideSplashBusiness(splashBusiness: SplashBusiness) {
        baseBusiness = splashBusiness
    }

    fun checkFirstOpen(){
        if (Waleed.getInstance().getAppPref().
            getBoolean(AppConstants.PREF_KEY_IS_FIRST_OPEN,false)){
            view?.openHomePage()
        }else{

            view?.showLanguageLayout()
        }
    }
}