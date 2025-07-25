package presentation.viewmodel


import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import domain.models.Weather

sealed class WeatherViewState {
    abstract val isLoading: Boolean

    data object Initial : WeatherViewState() {
        override val isLoading: Boolean = false
    }

    data class Loading @DefaultArgumentInterop.Enabled constructor(
        override val isLoading: Boolean = true
    ) : WeatherViewState()

    data class Success @DefaultArgumentInterop.Enabled constructor(
        val weather: Weather,
        override val isLoading: Boolean = false
    ) : WeatherViewState()

    data class Error @DefaultArgumentInterop.Enabled constructor(
        val error: String,
        override val isLoading: Boolean = false
    ) : WeatherViewState()
}