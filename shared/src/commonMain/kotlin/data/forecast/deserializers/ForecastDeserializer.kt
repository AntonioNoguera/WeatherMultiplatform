package data.forecast.deserializers

import data.core.deserializers.BaseDeserializer
import data.core.deserializers.DeserializationException
import data.core.extensions.getObjectOrNull
import data.core.extensions.getRequiredArray
import data.core.extensions.getRequiredDouble
import data.core.extensions.getStringOrNull
import data.forecast.dto.ForecastDTO
import domain.forecast.models.ForecastModel
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class ForecastDeserializer : BaseDeserializer<ForecastDTO, ForecastModel>() {
    override fun deserialize(jsonString: String): ForecastDTO {
        TODO("Not yet implemented")
    }

    override fun deserializeWithValidation(jsonString: String): ForecastDTO {
        TODO("Not yet implemented")
    }

    /**
     * Método adicional para deserializar respuestas de forecast
     */
    fun deserializeForecastResponse(jsonString: String): List<ForecastDTO> {

        val jsonObject = super.safeParseJson(jsonString)
            ?: throw DeserializationException("Invalid forecast JSON format")

        val listArray = jsonObject.getRequiredArray("list", "Forecast list")

        return listArray.map { element ->
            deserializeForecast(element.jsonObject)
        }
    }

    private fun deserializeForecast (jsonObject: JsonObject) : ForecastDTO {

        val main : JsonObject = jsonObject.getObjectOrNull("main") ?: JsonObject(emptyMap())
        val currentWeather : JsonArray = jsonObject.getRequiredArray("weather")
        val currentDescription : List<String> = currentWeather.map { it.jsonObject.getStringOrNull("description") ?: "" }

        return ForecastDTO(
            temperature = main.getRequiredDouble("temp_max"),
            humidity = main.getRequiredDouble("humidity"),
            description = currentDescription.first(),
            timeStamp = jsonObject.getStringOrNull("dt_txt") ?: ""
        )
    }
}