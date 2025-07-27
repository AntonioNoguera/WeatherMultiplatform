package data.weather

import data.weather.deserializers.Coordinates
import data.weather.deserializers.WeatherDeserializer
import data.weather.dto.WeatherResponse
import data.weather.dto.toDomain
import domain.weather.models.Weather
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

 /**
  * API Client [ HttpClientEngine propio para multiplataforma ]
  */
class WeatherAPI(engine: HttpClientEngine) {
 private val apiKey = "02be332837e16e0b93a5173d98a9f6cb"
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
 private val deserializer = WeatherDeserializer()

 /**
  * Validaciones personalizadas
  */
 suspend fun getCurrentWeatherValidated(cityName: String): WeatherResponse {
     val jsonResponse = makeWeatherRequest(cityName)
     return deserializer.deserializeWithValidation(jsonResponse)
 }

 /**
  * Clima con coordenadas
  */
 suspend fun getCurrentWeatherWithCoordinates(cityName: String): Pair<Weather, Coordinates> {
     val jsonResponse = makeWeatherRequest(cityName)
     val (weatherResponse, coordinates) = deserializer.deserializeWeatherWithCoordinates(jsonResponse)
     return weatherResponse.toDomain() to coordinates
 }

 /**
  * Obtiene pronóstico
  */
 suspend fun getForecast(cityName: String, days: Int = 5): List<Weather> {
     val jsonResponse = client.get("$baseUrl/forecast") {
         parameter("q", cityName)
         parameter("appid", apiKey)
         parameter("units", "metric")
         parameter("lang", "es")
         parameter("cnt", days * 8)
     }.bodyAsText()

     return deserializer.deserializeForecastResponse(jsonResponse)
         .map { it.toDomain() }
 }

 /**
  * Método helper para hacer requests básicos
  */
 private suspend fun makeWeatherRequest(cityName: String): String {
     return client.get("$baseUrl/weather") {
         parameter("q", cityName)
         parameter("appid", apiKey)
         parameter("units", "metric")
         parameter("lang", "es")
     }.bodyAsText()
 }

 /**
  * Obtener weather por coordenadas
  */
 suspend fun getCurrentWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
     val jsonResponse = client.get("$baseUrl/weather") {
         parameter("lat", lat)
         parameter("lon", lon)
         parameter("appid", apiKey)
         parameter("units", "metric")
         parameter("lang", "es")
     }.bodyAsText()

     return deserializer.deserialize(jsonResponse)
 }
}