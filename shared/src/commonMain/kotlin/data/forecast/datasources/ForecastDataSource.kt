package data.forecast.datasources

import data.core.WeatherAPI
import domain.forecast.models.ForecastModel
import domain.forecast.repositories.ForecastRepository

class ForecastDataSource( private val weatherApi: WeatherAPI) : ForecastRepository {
    override suspend fun getForecast(cityName: String): List<ForecastModel> {
        val response = weatherApi.getForecast(cityName)
        return response.map {it.toDomain()}
    }
}