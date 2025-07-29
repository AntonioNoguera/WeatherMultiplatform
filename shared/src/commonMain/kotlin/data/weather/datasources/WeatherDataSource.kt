package data.weather.datasources

import data.core.WeatherAPI

import domain.weather.models.WeatherModel
import domain.weather.respositories.WeatherRepository

class WeatherDataSource(private val weatherApi: WeatherAPI) : WeatherRepository {
    override suspend fun getCurrentWeather(cityName: String): WeatherModel {
        val response = weatherApi.getCurrentWeatherValidated(cityName)
        return response.toDomain()
    }
}