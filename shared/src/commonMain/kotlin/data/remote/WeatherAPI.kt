package data.remote

import io.ktor.client.HttpClient

import io.ktor.client.call.*
import io.ktor.client.request.*

import data.remote.dto.WeatherResponse

class WeatherAPI(
    private val httpClient: HttpClient
) {
    private val apiKey = "02be332837e16e0b93a5173d98a9f6cb" // Mover esto al secrets
    private val baseUrl = "https://api.openweathermap.org/data/2.5"

    suspend fun getCurrentWeather(cityName: String): WeatherResponse {
        return httpClient.get("$baseUrl/weather") {
            parameter("q", cityName)
            parameter("appid", apiKey)
            parameter("units", "metric")
        }.body()
    }
}