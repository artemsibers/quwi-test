package com.quwi.testapp

import android.app.Application
import androidx.viewbinding.BuildConfig
import timber.log.Timber

class QuwiApp : Application(){
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}