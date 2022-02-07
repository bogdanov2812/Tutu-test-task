package com.bogdanov.tutu

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import com.bogdanov.tutu.data.local.database.Database
import com.bogdanov.tutu.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@ExperimentalPagingApi
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            modules(appModule)
        }
        Database.init(this)
    }
}