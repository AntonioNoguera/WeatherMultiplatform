package domain.forecast.models

data class ForecastModel (
    val temperature: Double,
    val humidity: Double,
    val description: String,
    val timeStamp: String
)
