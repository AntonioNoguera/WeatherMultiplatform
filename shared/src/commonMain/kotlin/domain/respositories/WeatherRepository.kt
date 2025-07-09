package domain.respositories

import domain.models.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(cityName: String): Weather
}