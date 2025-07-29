//
//  WeatherViewModelWrapper.swift
//  iosApp
//
//  Created by MICHAEL NOGUERA GUZMAN on 29/07/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import shared
import Combine

class WeatherViewModelWrapper: ObservableObject {
    private let viewModel: WeatherViewModel
    private var job: Task<Void, Never>?

    
    @Published var uiState: WeatherViewStateInterop
    
    init(viewModel: WeatherViewModel = KotlinDependencies.shared.getWeatherViewModel()) {
        self.viewModel = viewModel
        
        self.uiState = WeatherViewStateInterop(
            currentWeather: WeatherViewDataState.Initial(),
            forecast: ForecastViewDataState.Initial(),
            isRefreshing: false
        )
        
        self.job = Task {
            for await state in viewModel.uiStateInterop {
                self.uiState = state
            }
        }

        Task {
            try? await viewModel.activate()
        }
    }
    
    deinit {
        job?.cancel()
        viewModel.onCleared()
    }
    
    func fetchWeather(city: String) {
        Task {
            try? await viewModel.fetchWeather(city: city)
        }
    }
    
    func fetchForecast(city: String) {
        Task {
            try? await viewModel.fetchForecast(city: city)
        }
    }
    
    func clearWeatherError() {
        viewModel.clearWeatherError()
    }
    
    func clearForecastError() {
        viewModel.clearForecastError()
    }
}
