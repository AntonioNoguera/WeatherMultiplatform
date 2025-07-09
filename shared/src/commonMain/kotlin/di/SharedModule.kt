package di

import data.remote.WeatherAPI
import data.respositories.WeatherRepositoryImpl
import domain.respositories.WeatherRepository
import domain.useCases.GetWeatherUseCase
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val sharedModule = module {

    // HttpClient - configuración de red
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    // API
    single { WeatherAPI(get()) }

    // Repository
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }

    // Use Cases
    single { GetWeatherUseCase(get()) }
}