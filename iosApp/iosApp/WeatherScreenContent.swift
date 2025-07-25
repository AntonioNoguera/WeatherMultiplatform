//
//  WeatherScreenContent.swift
//  iosApp
//
//  Created by MICHAEL NOGUERA GUZMAN on 25/07/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI 
import shared


struct WeatherScreenContent: View {
    var state: WeatherViewState
    var onSearchWeather: (String) -> Void
    var onClearError: () -> Void
    
    @State private var cityInput: String = ""

    var body: some View {
        VStack(spacing: 20) {
            Text("🌤️ Weather App")
                .font(.largeTitle)
                .fontWeight(.bold)
                .padding(.top, 40)
            
            // Campo de búsqueda
            TextField("Ciudad", text: $cityInput)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding(.horizontal)
            
            // Botón de búsqueda
            Button(action: {
                if !cityInput.trimmingCharacters(in: .whitespaces).isEmpty {
                    onSearchWeather(cityInput.trimmingCharacters(in: .whitespaces))
                }
            }) {
                HStack {
                    if state.isLoading {
                        ProgressView()
                            .scaleEffect(0.8)
                        Text("Buscando...")
                    } else {
                        Text("Buscar")
                    }
                }
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(10)
            }
            .disabled(cityInput.trimmingCharacters(in: .whitespaces).isEmpty || state.isLoading)
            .padding(.horizontal)
            
            Spacer()
            
            // Contenido basado en el estado
            weatherContent
            
            Spacer()
        }
    }
    
    var weatherContent: some View {
        Group {
            switch onEnum(of: state) {
            case .initial:
                Text("Ingresa el nombre de una ciudad")
                    .foregroundColor(.gray)
            case .loading:
                VStack {
                    ProgressView()
                    Text("Buscando información del clima...")
                        .foregroundColor(.gray)
                }
            case .success(let success):
                WeatherCard(weather: success.weather)
            case .error(let error):
                ErrorCard(error: error.error, onDismiss: onClearError)
            }
        }
    }
}

struct WeatherCard: View {
    var weather: Weather
    
    var body: some View {
        VStack(spacing: 16) {
            Text(weather.cityName)
                .font(.title)
                .fontWeight(.bold)
            
            Text("\(Int(weather.temperature))°C")
                .font(.system(size: 60, weight: .light))
                .foregroundColor(.blue)
            
            Text(weather.description.capitalized)
                .font(.body)
                .foregroundColor(.gray)
            
            HStack(spacing: 40) {
                WeatherDetail(
                    icon: "💧",
                    label: "Humedad",
                    value: "\(weather.humidity)%"
                )
                WeatherDetail(
                    icon: "💨",
                    label: "Viento",
                    value: "\(Int(weather.windSpeed)) km/h"
                )
            }
        }
        .padding(24)
        .background(Color(.systemBackground))
        .cornerRadius(16)
        .shadow(radius: 8)
        .padding(.horizontal)
    }
}

struct WeatherDetail: View {
    var icon: String
    var label: String
    var value: String
    
    var body: some View {
        VStack(spacing: 4) {
            Text(icon)
                .font(.title2)
            Text(label)
                .font(.caption)
                .foregroundColor(.gray)
            Text(value)
                .font(.body)
                .fontWeight(.medium)
        }
    }
}


struct ErrorCard: View {
    var error: String
    var onDismiss: () -> Void
    
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            HStack {
                Text("❌ Error")
                    .fontWeight(.bold)
                Spacer()
                Button("✕") {
                    onDismiss()
                }
            }
            Text(error)
        }
        .padding()
        .background(Color.red.opacity(0.1))
        .cornerRadius(10)
        .padding(.horizontal)
    }
}
