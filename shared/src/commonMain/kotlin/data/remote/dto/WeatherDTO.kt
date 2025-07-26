package data.remote.dto


import domain.models.Weather
import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;

@Serializable
data class WeatherResponse(
    @SerialName("name") val name: String,
    @SerialName("main") val main: MainData,
    @SerialName("weather") val weather: List<WeatherData>,
    @SerialName("wind") val wind: WindData
)

@Serializable
data class MainData(
    @SerialName("temp") val temp: Double,
    @SerialName("humidity") val humidity: Int
)

@Serializable
data class WeatherData(
    @SerialName("main") val main: String,
    @SerialName("description") val description: String
)

@Serializable
data class WindData(
    @SerialName("speed") val speed: Double
)

fun WeatherResponse.toDomain(): Weather {
    return Weather(
        cityName = name,
        temperature = main.temp,
        description = weather.firstOrNull()?.description ?: "N/A",
        humidity = main.humidity,
        windSpeed = wind.speed
    )
}