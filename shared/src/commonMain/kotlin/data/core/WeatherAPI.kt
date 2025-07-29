package data.core

import data.forecast.deserializers.ForecastDeserializer
import data.weather.deserializers.Coordinates
import data.weather.deserializers.WeatherDeserializer
import data.forecast.dto.ForecastDTO
import data.weather.dto.WeatherDTO
import domain.weather.models.WeatherModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

 /**  API Client [ HttpClientEngine nativo para cada plataforma ] */

class WeatherAPI(engine: HttpClientEngine) {
 private val apiKey = "361f9d5c91955f40cf9e4ad53655e178"
 private val baseUrl = "https://api.openweathermap.org/data/2.5"

 private val client = HttpClient(engine) {
     expectSuccess = true
     install(ContentNegotiation) {
         json(Json {
             ignoreUnknownKeys = true
             prettyPrint = true
             isLenient = true
         })
     }

     install(HttpTimeout) {
         val timeout = 30000L
         connectTimeoutMillis = timeout
         requestTimeoutMillis = timeout
         socketTimeoutMillis = timeout
     }
 }

    // Instancia del deserializador
    private val weatherDeserializer = WeatherDeserializer()
    private val forecastDeserializer = ForecastDeserializer()


 /** Validaciones personalizadas */
 suspend fun getCurrentWeatherValidated(cityName: String): WeatherDTO {
     val jsonResponse = makeWeatherRequest(cityName)
     return weatherDeserializer.deserializeWithValidation(jsonResponse)
 }

 /** Clima con coordenadas */
 suspend fun getCurrentWeatherWithCoordinates(cityName: String): Pair<WeatherModel, Coordinates> {
     val jsonResponse = makeWeatherRequest(cityName)
     val (weatherResponse, coordinates) = weatherDeserializer.deserializeWeatherWithCoordinates(jsonResponse)
     return weatherResponse.toDomain() to coordinates
 }

 /** Obtiene pronóstico */
 suspend fun getForecast(cityName: String, days: Int = 5): List<ForecastDTO> {

     val jsonResponse = client.get("$baseUrl/forecast") {
         parameter("q", cityName)
         parameter("appid", apiKey)
         parameter("units", "metric")
         parameter("lang", "es")

         println("➡️ URL construida: ${url.buildString()}")

     }.bodyAsText()

     val deserialize = forecastDeserializer.deserializeForecastResponse(jsonResponse)

     return deserialize
 }

 /**  Metodo helper para hacer requests básicos */
 private suspend fun makeWeatherRequest(cityName: String): String {
     return client.get("$baseUrl/weather") {
         parameter("q", cityName)
         parameter("appid", apiKey)
         parameter("units", "metric")
         parameter("lang", "es")


         println("➡️ URL construida: ${url.buildString()}")
     }.bodyAsText()
 }

 /**  Obtener weather por coordenadas */
suspend fun getCurrentWeatherByCoordinates(lat: Double, lon: Double): WeatherDTO {
     val jsonResponse = client.get("$baseUrl/weather") {
         parameter("lat", lat)
         parameter("lon", lon)
         parameter("appid", apiKey)
         parameter("lang", "es")
     }.bodyAsText()

     return weatherDeserializer.deserialize(jsonResponse)
    }
}