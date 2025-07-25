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

    @State
    var viewModel: WeatherViewModel?

    @State
    var weatherState: WeatherViewState = .Initial.shared

    var body: some View {
        WeatherScreenContent(
            state: weatherState,
            onSearchWeather: { cityName in
                Task {
                    try? await viewModel?.searchWeather(cityName: cityName)
                }
            },
            onClearError: {
                viewModel?.clearError()
            }
        )
        .task {
            let viewModel = KotlinDependencies.shared.getWeatherViewModel()
            await withTaskCancellationHandler(
                operation: {
                    self.viewModel = viewModel
                    Task {
                        try? await viewModel.activate()
                    }
                    for await weatherState in viewModel.weatherState {
                        self.weatherState = weatherState
                    }
                },
                onCancel: {
                    viewModel.clear()
                    self.viewModel = nil
                }
            )
        }
    }
}
