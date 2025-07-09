import data.remote.WeatherAPI
import di.initKoin
import domain.respositories.WeatherRepository
import domain.useCases.GetWeatherUseCase

import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.AfterTest

class IntegrationTest : KoinTest {

    private val weatherApi: WeatherAPI by inject()
    private val weatherRepository: WeatherRepository by inject()
    private val getWeatherUseCase: GetWeatherUseCase by inject()

    @Test
    fun `should inject all dependencies correctly`() {
        // Arrange
        initKoin()

        // Assert - verifica que todas las dependencias se crean
        assertNotNull(weatherApi)
        assertNotNull(weatherRepository)
        assertNotNull(getWeatherUseCase)

        println("✅ WeatherApi: ${weatherApi::class.simpleName}")
        println("✅ WeatherRepository: ${weatherRepository::class.simpleName}")
        println("✅ GetWeatherUseCase: ${getWeatherUseCase::class.simpleName}")
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }
}