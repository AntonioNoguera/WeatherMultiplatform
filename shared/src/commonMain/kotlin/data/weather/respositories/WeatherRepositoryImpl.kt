package data.weather.respositories

import data.weather.WeatherAPI
import data.weather.dto.toDomain
import domain.weather.models.Weather
import domain.weather.respositories.WeatherRepository

class WeatherRepositoryImpl( private val weatherApi: WeatherAPI) : WeatherRepository {
    override suspend fun getCurrentWeather(cityName: String): Weather {
        val response = weatherApi.getCurrentWeatherValidated(cityName)
        return response.toDomain()
    }

    override suspend fun getForecast(cityName: String): List<Weather> {
        val response = weatherApi.getForecast(cityName)
        return response.map { it.toDomain()}
    }
}