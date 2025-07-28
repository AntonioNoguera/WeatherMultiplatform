package domain.weather.respositories

import domain.weather.models.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): Weather
    suspend fun getForecast(cityName: String): List<Weather>
}