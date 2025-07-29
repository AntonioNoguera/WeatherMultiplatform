package presentation.weather

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import domain.weather.models.WeatherModel
import presentation.core.ViewState

sealed class WeatherViewDataState {
    abstract val isLoading: Boolean

    data object Initial : WeatherViewDataState() {
        override val isLoading: Boolean = false
    }

    data class Loading @DefaultArgumentInterop.Enabled constructor(
        override val isLoading: Boolean = true
    ) : WeatherViewDataState()

    data class Success @DefaultArgumentInterop.Enabled constructor(
        val weather: WeatherModel,
        override val isLoading: Boolean = false
    ) : WeatherViewDataState()

    data class Error @DefaultArgumentInterop.Enabled constructor(
        val error: String,
        override val isLoading: Boolean = false
    ) : WeatherViewDataState()

    companion object {
        fun from(state: ViewState<WeatherModel>): WeatherViewDataState = when (state) {
            is ViewState.Initial -> Initial
            is ViewState.Loading -> Loading()
            is ViewState.Success -> Success(state.data)
            is ViewState.Error -> Error(state.error)
        }
    }
}