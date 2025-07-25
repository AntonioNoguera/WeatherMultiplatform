package presentation.viewmodel

// commonMain/models/WeatherViewModel.kt
import domain.useCases.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import presentation.ViewModel

class WeatherViewModel(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val mutableWeatherState: MutableStateFlow<WeatherViewState> =
        MutableStateFlow(WeatherViewState.Initial)

    val weatherState: StateFlow<WeatherViewState> = mutableWeatherState.asStateFlow()

    /**
     * Activates this viewModel so that `weatherState` returns the current weather state.
     */
    suspend fun activate() {
        // Si necesitas inicialización específica
//        log.d("WeatherViewModel activated")
    }

    override fun onCleared() {
//        log.v("Clearing WeatherViewModel")
    }

    suspend fun searchWeather(cityName: String) {
        if (cityName.isBlank()) return

        mutableWeatherState.update { WeatherViewState.Loading() }

        try {
            getWeatherUseCase(cityName)
                .onSuccess { weather ->
                    mutableWeatherState.update {
                        WeatherViewState.Success(weather = weather)
                    }
                }
                .onFailure { exception ->
                    handleWeatherError(exception)
                }
        } catch (exception: Exception) {
            handleWeatherError(exception)
        }
    }

    fun clearError() {
        mutableWeatherState.update { currentState ->
            when (currentState) {
                is WeatherViewState.Error -> WeatherViewState.Initial
                else -> currentState
            }
        }
    }

    private fun handleWeatherError(throwable: Throwable) {
//        log.e(throwable) { "Error getting weather data" }
        mutableWeatherState.update {
            WeatherViewState.Error(
                error = throwable.message ?: "Unknown error occurred"
            )
        }
    }
}