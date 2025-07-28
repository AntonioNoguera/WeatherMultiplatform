package domain.weather.useCases

import domain.weather.models.Weather
import domain.weather.respositories.WeatherRepository

class GetForecastUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Result<List<Weather>> {
        return try {
            val foreCast = repository.getForecast(cityName)
            Result.success(foreCast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}