package data.weather.dto

import domain.weather.models.WeatherModel

data class WeatherDTO(
    val name: String,
    val main: MainDataDTO,
    val weather: List<WeatherDataDTO>,
    val wind: WindDataDTO
){
    fun toDomain(): WeatherModel {
        return WeatherModel(
            cityName = name,
            temperature = main.temp,
            description = weather.firstOrNull()?.description ?: "N/A",
            humidity = main.humidity,
            windSpeed = wind.speed
        )
    }
}



