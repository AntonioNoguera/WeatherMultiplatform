package com.weather.app.android

import android.app.Application
import di.initKoin
import org.koin.android.ext.koin.androidContext
import com.weather.app.android.di.androidModule

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@WeatherApplication)
            modules(androidModule)
        }
    }
}