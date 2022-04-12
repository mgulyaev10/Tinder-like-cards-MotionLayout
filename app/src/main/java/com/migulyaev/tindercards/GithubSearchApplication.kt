package com.migulyaev.tindercards

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.migulyaev.tindercards.di.appModule
import com.migulyaev.tindercards.di.networkModule
import com.migulyaev.tindercards.di.searchModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GithubSearchApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GithubSearchApplication)
            modules(
                searchModule,
                networkModule,
                appModule
            )
        }
        Fresco.initialize(this)
    }

}