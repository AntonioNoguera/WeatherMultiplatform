// androidApp/src/main/kotlin/com/weather/app/android/WeatherApplication.kt
package com.weather.app.android

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import presentation.viewmodel.WeatherViewModel

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            module {
                single<Context> { this@WeatherApplication }

                // ViewModel sin logger
                viewModel { WeatherViewModel(get()) }

                single<SharedPreferences> {
                    get<Context>().getSharedPreferences(
                        "WEATHER_APP_SETTINGS",
                        Context.MODE_PRIVATE,
                    )
                }

                // Función de startup simple
                single {
                    { android.util.Log.i("WeatherApp", "Weather App initialized!") }
                }
            }
        )
    }
}