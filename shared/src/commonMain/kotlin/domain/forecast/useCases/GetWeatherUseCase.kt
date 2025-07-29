package domain.forecast.useCases

import domain.weather.models.WeatherModel
import domain.weather.respositories.WeatherRepository


class GetWeatherUseCase(private val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Result<WeatherModel> {
        return try {
            val weather = repository.getCurrentWeather(cityName)
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}