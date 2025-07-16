package com.weather.app.android.di

import org.koin.dsl.module
import presentation.viewmodel.WeatherViewModel

val androidModule = module {
    factory { WeatherViewModel(get()) }
}