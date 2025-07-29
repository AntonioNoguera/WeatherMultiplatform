package domain.weather.useCases

import domain.forecast.models.ForecastModel
import domain.forecast.repositories.ForecastRepository

class GetForecastUseCase(private val repository: ForecastRepository) {
    suspend operator fun invoke(cityName: String): Result<List<ForecastModel>> {
        return try {
            val foreCast = repository.getForecast(cityName)
            Result.success(foreCast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}