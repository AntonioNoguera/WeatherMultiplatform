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
import data.weather.dto.MainData
import data.weather.dto.WeatherData
import data.weather.dto.WeatherResponse
import data.weather.dto.WindData
import data.weather.dto.toDomain
import domain.weather.models.Weather
import kotlinx.serialization.json.*

/**
 * Deserializador específico para Weather API
 */
class WeatherDeserializer : BaseDeserializer<WeatherResponse, Weather>() {

    override fun deserialize(jsonString: String): WeatherResponse {
        println("Deserializing weather JSON to DTO using automatic serialization")
        return json.decodeFromString<WeatherResponse>(jsonString)
    }

    override fun deserializeWithValidation(jsonString: String): WeatherResponse {
        println("Deserializing weather JSON with custom validation")

        val jsonObject = safeParseJson(jsonString)
            ?: throw DeserializationException("Invalid JSON format")

        return WeatherResponse(
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

    private fun deserializeMainData(jsonObject: JsonObject): MainData {
        val mainObj = jsonObject.getRequiredObject("main", "Main weather data")

        val temperature = mainObj.getRequiredDouble("temp", "Temperature")
            .validateInRange(-100.0, 100.0, "Temperature")

        val humidity = mainObj.getRequiredInt("humidity", "Humidity")
            .validateInRange(0, 100, "Humidity")

        return MainData(
            temp = temperature,
            humidity = humidity
        )
    }

    private fun deserializeWeatherList(jsonObject: JsonObject): List<WeatherData> {
        val weatherArray = jsonObject.getRequiredArray("weather", "Weather conditions")

        if (weatherArray.isEmpty()) {
            throw DeserializationException("Weather array cannot be empty")
        }

        return weatherArray.map { element ->
            val weatherObj = element.jsonObject
            WeatherData(
                main = weatherObj.getStringOrNull("main").orEmpty(),
                description = weatherObj.getStringOrNull("description").orEmpty()
            )
        }
    }

    private fun deserializeWindData(jsonObject: JsonObject): WindData {
        // Wind data es opcional, usar objeto vacío si no existe
        val windObj = jsonObject.getObjectOrNull("wind") ?: JsonObject(emptyMap())

        val speed = windObj.getDoubleOrNull("speed")
            ?.validatePositive("Wind speed") ?: 0.0

        return WindData(speed = speed)
    }

    /**
     * Método adicional para deserializar respuestas de forecast
     */
    fun deserializeForecastResponse(jsonString: String): List<WeatherResponse> {
        print("Deserializing weather forecast JSON")

        val jsonObject = safeParseJson(jsonString)
            ?: throw DeserializationException("Invalid forecast JSON format")

        val listArray = jsonObject.getRequiredArray("list", "Forecast list")

        return listArray.map { element ->
            deserializeWithValidation(element.toString())
        }
    }

    /**
     * Método para deserializar respuestas con coordenadas
     */
    fun deserializeWeatherWithCoordinates(jsonString: String): Pair<WeatherResponse, Coordinates> {
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