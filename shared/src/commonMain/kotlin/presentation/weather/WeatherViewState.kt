package presentation.weather

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import domain.weather.models.Weather
import presentation.core.ViewState

data class WeatherViewState(
    val currentWeather: ViewState<Weather> = ViewState.Initial,
    val forecast: ViewState<List<Weather>> = ViewState.Initial,
    val isRefreshing: Boolean = false
)