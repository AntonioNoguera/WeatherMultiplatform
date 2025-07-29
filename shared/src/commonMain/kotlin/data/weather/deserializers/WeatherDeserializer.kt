package data.weather.deserializers


import data.core.deserializers.BaseDeserializer
import data.core.deserializers.DeserializationException
import data.core.extensions.getDoubleOrNull
import data.core.extensions.getObjectOrNull
import data.core.extensions.getRequiredArray
import data.core.extensions.getRequiredDouble
import data.core.extensions.getRequiredInt
import data.core.extensions.getRequiredObject
import data.core.extensions.getRequiredString
import data.core.extensions.getStringOrNull
import data.core.extensions.validateInRange
import data.core.extensions.validateNotEmpty
import data.core.extensions.validatePositive
import data.forecast.dto.ForecastDTO
import data.weather.dto.MainDataDTO
import data.weather.dto.WeatherDTO
import data.weather.dto.WeatherDataDTO
import data.weather.dto.WindDataDTO
import domain.weather.models.WeatherModel
import kotlinx.serialization.json.*

/**
 * Deserializador específico para Weather API
 */
class WeatherDeserializer : BaseDeserializer<WeatherDTO, WeatherModel>() {

    override fun deserialize(jsonString: String): WeatherDTO {
        println("Deserializing weather JSON to DTO using automatic serialization")
        return json.decodeFromString<WeatherDTO>(jsonString)
    }

    override fun deserializeWithValidation(jsonString: String): WeatherDTO {
        println("Deserializing weather JSON with custom validation")

        val jsonObject = safeParseJson(jsonString)
            ?: throw DeserializationException("Invalid JSON format")

        return WeatherDTO(
            name = deserializeCityName(jsonObject),
            main = deserializeMainData(jsonObject),
            weather = deserializeWeatherList(jsonObject),
            wind = deserializeWindData(jsonObject)
        )
    }

    private fun deserializeCityName(jsonObject: JsonObject): String {
        return jsonObject.getRequiredString("name", "City name")
            .validateNotEmpty("City name")
            .trim()
    }

    private fun deserializeMainData(jsonObject: JsonObject): MainDataDTO {
        val mainObj = jsonObject.getRequiredObject("main", "Main weather data")

        val temperature = mainObj.getRequiredDouble("temp", "Temperature")
            .validateInRange(-100.0, 100.0, "Temperature")

        val humidity = mainObj.getRequiredInt("humidity", "Humidity")
            .validateInRange(0, 100, "Humidity")

        return MainDataDTO(
            temp = temperature,
            humidity = humidity
        )
    }

    private fun deserializeWeatherList(jsonObject: JsonObject): List<WeatherDataDTO> {
        val weatherArray = jsonObject.getRequiredArray("weather", "Weather conditions")

        if (weatherArray.isEmpty()) {
            throw DeserializationException("Weather array cannot be empty")
        }

        return weatherArray.map { element ->
            val weatherObj = element.jsonObject
            WeatherDataDTO(
                main = weatherObj.getStringOrNull("main").orEmpty(),
                description = weatherObj.getStringOrNull("description").orEmpty()
            )
        }
    }

    private fun deserializeWindData(jsonObject: JsonObject): WindDataDTO {
        val windObj = jsonObject.getObjectOrNull("wind") ?: JsonObject(emptyMap())

        val speed = windObj.getDoubleOrNull("speed") ?.validatePositive("Wind speed") ?: 0.0

        return WindDataDTO(speed = speed)
    }

    fun deserializeWeatherWithCoordinates(jsonString: String): Pair<WeatherDTO, Coordinates> {
        val weatherResponse = deserializeWithValidation(jsonString)

        val jsonObject = safeParseJson(jsonString)!!
        val coordObj = jsonObject.getObjectOrNull("coord")

        val coordinates = if (coordObj != null) {
            Coordinates(
                lon = coordObj.getDoubleOrNull("lon") ?: 0.0,
                lat = coordObj.getDoubleOrNull("lat") ?: 0.0
            )
        } else {
            Coordinates(0.0, 0.0)
        }

        return weatherResponse to coordinates
    }
}

/**
 * Data class para coordenadas
 */
data class Coordinates(
    val lon: Double,
    val lat: Double
)