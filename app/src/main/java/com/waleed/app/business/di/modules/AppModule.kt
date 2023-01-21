package com.waleed.app.business.di.modules

import android.content.Context
import com.waleed.app.Waleed
import com.waleed.app.business.di.LocalPreference
import com.waleed.app.business.preference.AppPref
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    @LocalPreference
    internal fun provideLocalPreferenceDataSource(context: Context): AppPref {
        return AppPref(context)
    }

    @Singleton
    @Provides
    internal fun provideContext(application: Waleed): Context {
        return application.applicationContext
    }
}