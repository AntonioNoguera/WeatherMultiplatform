package data.mapper

import data.remote.dto.WeatherResponse
import domain.models.Weather

//Se puede mover esto desde el DTO? Ta raro un extension por acá

fun WeatherResponse.toDomain(): Weather {
    return Weather(
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "N/A",
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}