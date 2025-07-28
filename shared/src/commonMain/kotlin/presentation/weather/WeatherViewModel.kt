package presentation.weather

import domain.weather.models.Weather
import domain.weather.useCases.GetForecastUseCase
import domain.weather.useCases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import presentation.core.ViewModel
import presentation.core.ViewState

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherViewState())
    val uiState: StateFlow<WeatherViewState> = _uiState.asStateFlow()

    suspend fun fetchWeather(city: String) {
        _uiState.update { it.copy(currentWeather = ViewState.Loading()) }
        try {
            getWeatherUseCase(city).onSuccess { weather ->
                _uiState.update { it.copy(currentWeather = ViewState.Success(weather)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(currentWeather = ViewState.Error(exception.message ?: "Error")) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(currentWeather = ViewState.Error(e.message ?: "Error")) }
        }
    }

    suspend fun fetchForecast(city: String) {
        _uiState.update { it.copy(forecast = ViewState.Loading()) }
        try {
            getForecastUseCase(city).onSuccess { forecast ->
                _uiState.update { it.copy(forecast = ViewState.Success(forecast)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(forecast = ViewState.Error(exception.message ?: "Error")) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(forecast = ViewState.Error(e.message ?: "Error")) }
        }
    }

    fun clearWeatherError() {
        _uiState.update {
            if (it.currentWeather is ViewState.Error)
                it.copy(currentWeather = ViewState.Initial)
            else it
        }
    }

    fun clearForecastError() {
        _uiState.update {
            if (it.forecast is ViewState.Error)
                it.copy(forecast = ViewState.Initial)
            else it
        }
    }

    override fun onCleared() {
        println("Clearing WeatherViewModel")
    }

    suspend fun activate() {
        // Lógica inicial si aplica
    }
}