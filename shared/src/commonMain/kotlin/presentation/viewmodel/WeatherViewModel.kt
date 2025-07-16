package presentation.viewmodel

import domain.models.Weather
import domain.useCases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import presentation.BaseViewModel
import presentation.ui_state.WeatherUiState

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun searchWeather(cityName: String) {
        if (cityName.isBlank()) return

        launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getWeatherUseCase(cityName)
                .onSuccess { weather ->
                    _uiState.value = WeatherUiState(weather = weather)
                }
                .onFailure { exception ->
                    _uiState.value = WeatherUiState(
                        error = exception.message ?: "Unknown error"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}