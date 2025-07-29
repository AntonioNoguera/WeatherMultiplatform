package presentation.weather

//For the swift implementations
data class WeatherViewStateInterop(
    val currentWeather: WeatherViewDataState,
    val forecast: ForecastViewDataState,
    val isRefreshing: Boolean
)