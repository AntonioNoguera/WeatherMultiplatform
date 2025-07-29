package presentation.weather

import domain.forecast.models.ForecastModel
import domain.weather.models.WeatherModel
import presentation.core.ViewState

data class WeatherViewState(
    val currentWeather: ViewState<WeatherModel> = ViewState.Initial,
    val forecast: ViewState<List<ForecastModel>> = ViewState.Initial,
    val isRefreshing: Boolean = false
)