package com.waleed.app.business.di.modules

import com.waleed.app.business.network.LoggingInterceptor
import com.waleed.app.util.AppConstants.BASE_URL
import com.waleed.app.business.network.APIDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(LoggingInterceptor())
                .build()

        return Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideFindoodApi(retrofit: Retrofit): APIDataSource {
        return retrofit.create(APIDataSource::class.java)
    }


//    @Singleton
//    @Provides
//    fun provideFindoodNewApi(retrofit: Retrofit): FindoodNewAPIDataSource {
//        return retrofit.create(FindoodNewAPIDataSource::class.java)
//    }
}