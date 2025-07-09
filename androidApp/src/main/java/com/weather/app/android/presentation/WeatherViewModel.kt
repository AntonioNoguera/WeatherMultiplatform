package com.weather.app.android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.models.Weather
import domain.useCases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun searchWeather(cityName: String) {
        if (cityName.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getWeatherUseCase(cityName)
                .onSuccess { weather ->
                    _uiState.value = WeatherUiState(weather = weather)
                }
                .onFailure { exception ->
                    _uiState.value = WeatherUiState(
                        error = exception.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}