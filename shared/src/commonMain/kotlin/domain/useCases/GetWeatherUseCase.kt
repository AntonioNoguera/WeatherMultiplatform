package domain.useCases

import domain.models.Weather
import domain.respositories.WeatherRepository


class GetWeatherUseCase( val repository: WeatherRepository ) {
    suspend operator fun invoke(cityName: String): Result<Weather> {
        return try {
            val weather = repository.getCurrentWeather(cityName)
            Result.success(weather)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}