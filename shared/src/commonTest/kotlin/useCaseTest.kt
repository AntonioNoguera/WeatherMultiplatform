import domain.models.Weather
import domain.respositories.WeatherRepository
import domain.useCases.GetWeatherUseCase
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

import kotlinx.coroutines.test.runTest

class UseCaseTest {

    @Test
    fun `should return success when repository returns weather`() = runTest {
        // Arrange
        val fakeRepository = object : WeatherRepository {
            override suspend fun getCurrentWeather(cityName: String): Weather {
                return Weather(
                    cityName = cityName,
                    temperature = 20.0,
                    description = "Nublado",
                    humidity = 70,
                    windSpeed = 5.0
                )
            }
        }

        val useCase = GetWeatherUseCase(fakeRepository)

        // Act
        val result = useCase.invoke("Valencia")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Valencia", result.getOrNull()?.cityName)
        assertEquals(20.0, result.getOrNull()?.temperature)
        print("")
    }

    @Test
    fun `should return failure when repository throws exception`() = runTest {
        // Arrange
        val fakeRepository = object : WeatherRepository {
            override suspend fun getCurrentWeather(cityName: String): Weather {
                throw Exception("Network error")
            }
        }

        val useCase = GetWeatherUseCase(fakeRepository)

        // Act
        val result = useCase.invoke("Valencia")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}