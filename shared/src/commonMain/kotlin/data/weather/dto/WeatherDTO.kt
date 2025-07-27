package data.weather.dto

import domain.weather.models.Weather

data class WeatherResponse(
    val name: String,
    val main: MainData,
    val weather: List<WeatherData>,
    val wind: WindData
)

data class MainData(
    val temp: Double,
    val humidity: Int
)

data class WeatherData(
    val main: String,
    val description: String
)

data class WindData(
    val speed: Double
)

//Maybe la mejor practica es tener un clase para este tipo de mappers.
fun WeatherResponse.toDomain(): Weather {
    return Weather(
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "N/A",
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}