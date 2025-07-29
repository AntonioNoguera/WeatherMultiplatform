package data.forecast.dto

import domain.forecast.models.ForecastModel

class ForecastDTO (
    val temperature: Double,
    val humidity: Double,
    val description: String,
    val timeStamp: String
) {
    fun toDomain() : ForecastModel {
        return ForecastModel(
            temperature = temperature,
            humidity = humidity,
            description = description,
            timeStamp = timeStamp
        )
    }
}

