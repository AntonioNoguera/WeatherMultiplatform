package domain.forecast.repositories

import domain.forecast.models.ForecastModel

interface ForecastRepository {
    suspend fun getForecast(cityName: String): List<ForecastModel>
}
