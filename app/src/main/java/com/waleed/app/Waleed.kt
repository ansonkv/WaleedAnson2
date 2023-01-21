package com.waleed.app

import android.app.Application
import com.waleed.app.business.database.AppDataBase
import com.waleed.app.business.di.AppComponent
import com.waleed.app.business.di.DaggerAppComponent
import com.waleed.app.business.preference.AppPref

class Waleed : Application() {
    companion object {
        private lateinit var appComponent: AppComponent

        public lateinit var appContext: Waleed

        private lateinit var db: AppDataBase

        fun getInstance(): Waleed {
            return appContext
        }

        fun getAppComponent(): AppComponent {
            return appComponent
        }

        private lateinit var appPref: AppPref
    }

    override fun onCreate() {
        super.onCreate()
        appContext=this
        appComponent=initDagger(this)
        appPref= AppPref(this)

    }
    fun initDagger(application: Waleed): AppComponent {
        return DaggerAppComponent.builder().application(application).build()
    }

    fun getAppPref(): AppPref {
        return appPref
    }
}