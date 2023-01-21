package com.waleed.app.business.base

import android.content.Context
import com.waleed.app.business.di.LocalPreference
import com.waleed.app.business.preference.AppPref
import com.waleed.app.business.network.APIDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject internal constructor(private val context: Context,
                                                 private val remote: APIDataSource,
                                                 @param:LocalPreference private val preference:AppPref){

}