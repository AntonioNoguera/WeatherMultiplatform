package domain.weather.respositories

import domain.weather.models.WeatherModel

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): WeatherModel
}