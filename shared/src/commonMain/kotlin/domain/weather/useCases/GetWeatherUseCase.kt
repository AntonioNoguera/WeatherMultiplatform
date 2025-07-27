package domain.weather.useCases

import domain.weather.models.Weather
import domain.weather.respositories.WeatherRepository


class GetWeatherUseCase( val repository: WeatherRepository) {
    suspend operator fun invoke(cityName: String): Result<Weather> {
        return try {
            val weather = repository.getCurrentWeather(cityName)
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}