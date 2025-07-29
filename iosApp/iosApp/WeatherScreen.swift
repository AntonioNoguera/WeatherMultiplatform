//
//  WeatherScreen.swift
//  iosApp
//
//  Created by MICHAEL NOGUERA GUZMAN on 25/07/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import shared

struct WeatherScreen: View {

    @StateObject
    private var viewModel = WeatherViewModelWrapper()

    var body: some View {
        WeatherScreenContent(
            state: viewModel.uiState,
            onSearchWeather: { cityName in
                viewModel.fetchWeather(city: cityName)
                viewModel.fetchForecast(city: cityName)
            },
            onClearError: {
                viewModel.clearWeatherError()
                viewModel.clearForecastError()
            }
        )
    }
}
