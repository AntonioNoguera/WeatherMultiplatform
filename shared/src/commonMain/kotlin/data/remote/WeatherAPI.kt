 package data.remote

    import io.ktor.client.HttpClient
    import io.ktor.client.engine.HttpClientEngine
    import io.ktor.client.call.*
    import io.ktor.client.request.*
    import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
    import io.ktor.client.plugins.HttpTimeout
    import io.ktor.serialization.kotlinx.json.json
    import data.remote.dto.WeatherResponse
    import kotlinx.serialization.json.Json

 class WeatherAPI(engine: HttpClientEngine) {
        private val apiKey = "02be332837e16e0b93a5173d98a9f6cb" // Mover esto al secrets
        private val baseUrl = "https://api.openweathermap.org/data/2.5"

        private val client = HttpClient(engine) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true //Para que el DTO decodifique los datos que le solicitamos.
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

        suspend fun getCurrentWeather(cityName: String): WeatherResponse {


            return client.get("$baseUrl/weather") {
                parameter("q", cityName)
                parameter("appid", apiKey)
                parameter("units", "metric")
            }.body()
        }
    }