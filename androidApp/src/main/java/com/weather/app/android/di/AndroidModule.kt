package com.weather.app.android.di

import com.weather.app.android.presentation.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { WeatherViewModel(get()) }
}