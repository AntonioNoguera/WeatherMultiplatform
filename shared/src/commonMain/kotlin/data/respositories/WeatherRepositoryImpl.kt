package data.respositories

import data.mapper.toDomain
import data.remote.WeatherAPI
import domain.models.Weather
import domain.respositories.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherApi: WeatherAPI
) : WeatherRepository {

    override suspend fun getCurrentWeather(cityName: String): Weather {
        val response = weatherApi.getCurrentWeather(cityName)
        return response.toDomain()
    }
}