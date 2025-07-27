import data.weather.deserializers.WeatherDeserializer
import kotlin.test.*

class DeserealizationTest {

    private val deserializer = WeatherDeserializer()

    // JSON de ejemplo similar al que devuelve OpenWeatherMap
    private val validJsonResponse = """
        {
            "coord": {
                "lon": -100.3167,
                "lat": 25.6667
            },
            "weather": [
                {
                    "id": 801,
                    "main": "Clouds",
                    "description": "pocas nubes",
                    "icon": "02d"
                }
            ],
            "base": "stations",
            "main": {
                "temp": 28.5,
                "feels_like": 31.2,
                "temp_min": 27.1,
                "temp_max": 30.0,
                "pressure": 1013,
                "humidity": 65
            },
            "visibility": 10000,
            "wind": {
                "speed": 3.5,
                "deg": 120
            },
            "clouds": {
                "all": 20
            },
            "dt": 1640995200,
            "sys": {
                "type": 1,
                "id": 4248,
                "country": "MX",
                "sunrise": 1640962800,
                "sunset": 1641000000
            },
            "timezone": -21600,
            "id": 3995465,
            "name": "Monterrey",
            "cod": 200
        }
    """.trimIndent()

    // JSON con datos problemáticos para probar validaciones
    private val problematicJsonResponse = """
        {
            "name": "",
            "main": {
                "temp": 150.0,
                "humidity": 150
            },
            "weather": [],
            "wind": {
                "speed": -5.0
            }
        }
    """.trimIndent()

    // JSON completamente malformado
    private val invalidJsonResponse = """
        {
            "name": "Monterrey",
            "weather": [
                {
                    "description": "soleado"
                }
            ]
        }
    """.trimIndent()

    @Test
    fun `test deserializeToDto with valid JSON`() {
        println("🧪 Testing deserializeToDto with valid JSON")

        // 🔍 PUNTO DE DEBUG: Coloca breakpoint aquí para ver el JSON de entrada
        val result = deserializer.deserializeToDto(validJsonResponse)

        // 🔍 PUNTO DE DEBUG: Coloca breakpoint aquí para inspeccionar el resultado
        println("✅ Resultado DTO:")
        println("   Ciudad: ${result.name}")
        println("   Temperatura: ${result.main.temp}°C")
        println("   Descripción: ${result.weather.firstOrNull()?.description}")
        println("   Humedad: ${result.main.humidity}%")
        println("   Viento: ${result.wind.speed} m/s")

        // Verificaciones
        assertEquals("Monterrey", result.name)
        assertEquals(28.5, result.main.temp)
        assertEquals("pocas nubes", result.weather.first().description)
        assertEquals(65, result.main.humidity)
        assertEquals(3.5, result.wind.speed)
    }

    @Test
    fun `test deserializeToDomain converts DTO to domain model`() {
        println("🧪 Testing deserializeToDomain conversion")

        // 🔍 PUNTO DE DEBUG: Breakpoint para ver la deserialización completa
        val domainResult = deserializer.deserializeToDomain(validJsonResponse)

        println("✅ Resultado Domain Model:")
        println("   Ciudad: ${domainResult.cityName}")
        println("   Temperatura: ${domainResult.temperature}°C")
        println("   Descripción: ${domainResult.description}")
        println("   Humedad: ${domainResult.humidity}%")
        println("   Viento: ${domainResult.windSpeed} m/s")

        // Verificaciones del modelo de dominio
        assertEquals("Monterrey", domainResult.cityName)
        assertEquals(28.5, domainResult.temperature)
        assertEquals("pocas nubes", domainResult.description)
        assertEquals(65, domainResult.humidity)
        assertEquals(3.5, domainResult.windSpeed)
    }

    @Test
    fun `test deserializeWithValidation handles missing required fields`() {
        println("🧪 Testing validation with incomplete JSON")

        try {
            // 🔍 PUNTO DE DEBUG: Breakpoint aquí para ver cómo maneja datos faltantes
            val result = deserializer.deserializeWithValidation(invalidJsonResponse)
            fail("Expected exception but got result: $result")
        } catch (e: Exception) {
            println("✅ Validación funcionó correctamente:")
            println("   Error capturado: ${e.message}")

            // 🔍 PUNTO DE DEBUG: Inspecciona el tipo de error
            assertTrue(e.message?.contains("required") == true)
        }
    }

    @Test
    fun `test deserializeWithValidation applies data corrections`() {
        println("🧪 Testing data correction in validation")

        // JSON con datos que necesitan corrección
        val jsonWithBadData = """
            {
                "name": "  Monterrey  ",
                "main": {
                    "temp": 150.0,
                    "humidity": 150
                },
                "weather": [
                    {
                        "main": "Clear",
                        "description": "cielo claro"
                    }
                ],
                "wind": {
                    "speed": -2.5
                }
            }
        """.trimIndent()

        // 🔍 PUNTO DE DEBUG: Observa las correcciones aplicadas
        val result = deserializer.deserializeWithValidation(jsonWithBadData)

        println("✅ Correcciones aplicadas:")
        println("   Nombre original: '  Monterrey  ' -> Procesado: '${result.name}'")
        println("   Temperatura original: 150.0 -> Corregida: ${result.main.temp}")
        println("   Humedad original: 150 -> Corregida: ${result.main.humidity}")
        println("   Velocidad viento original: -2.5 -> Corregida: ${result.wind.speed}")

        // Verificar que las correcciones se aplicaron
        assertEquals("Monterrey", result.name) // Debería estar trimmed
        assertEquals(100.0, result.main.temp) // Debería estar limitada a 100
        assertEquals(100, result.main.humidity) // Debería estar limitada a 100
        assertEquals(0.0, result.wind.speed) // Velocidad negativa corregida a 0
    }

    @Test
    fun `test deserializer handles empty weather array`() {
        println("🧪 Testing empty weather array handling")

        val jsonWithEmptyWeather = """
            {
                "name": "Monterrey",
                "main": {
                    "temp": 25.0,
                    "humidity": 60
                },
                "weather": [],
                "wind": {
                    "speed": 2.0
                }
            }
        """.trimIndent()

        try {
            // 🔍 PUNTO DE DEBUG: Ver cómo maneja array vacío
            val result = deserializer.deserializeWithValidation(jsonWithEmptyWeather)
            fail("Expected exception for empty weather array")
        } catch (e: Exception) {
            println("✅ Manejo correcto de array vacío:")
            println("   Error: ${e.message}")
            assertTrue(e.message?.contains("empty") == true)
        }
    }

    @Test
    fun `test deserializeWeatherWithCoordinates extracts coordinates`() {
        println("🧪 Testing coordinate extraction")

        // 🔍 PUNTO DE DEBUG: Ver extracción de coordenadas
        val (weatherResponse, coordinates) = deserializer.deserializeWeatherWithCoordinates(validJsonResponse)

        println("✅ Coordenadas extraídas:")
        println("   Latitud: ${coordinates.lat}")
        println("   Longitud: ${coordinates.lon}")
        println("   Ciudad: ${weatherResponse.name}")

        assertEquals(-100.3167, coordinates.lon, 0.001)
        assertEquals(25.6667, coordinates.lat, 0.001)
        assertEquals("Monterrey", weatherResponse.name)
    }


    @Test
    fun `test step by step deserialization process`() {
        println("🧪 Testing step-by-step deserialization")

        // 🔍 PUNTO DE DEBUG PRINCIPAL: Coloca breakpoint aquí
        println("📥 JSON de entrada:")
        println(validJsonResponse.take(200) + "...")

        // Paso 1: Parsear JSON
        val jsonObject = deserializer.safeParseJson(validJsonResponse)

        // 🔍 PUNTO DE DEBUG: Inspecciona el JsonObject parseado
        println("\n📊 JsonObject parseado:")
        jsonObject?.let { obj ->
            obj.keys.take(5).forEach { key ->
                val value = obj[key].toString()
                println("   $key: ${value.take(30)}${if (value.length > 30) "..." else ""}")
            }
        }

        // Paso 2: Probar deserialización básica
        val basicResult = deserializer.deserializeToDto(validJsonResponse)

        // 🔍 PUNTO DE DEBUG: Ver resultado básico
        println("\n🔄 Deserialización básica:")
        println("   Tipo: ${basicResult::class.simpleName}")
        println("   Ciudad: ${basicResult.name}")
        println("   Temp: ${basicResult.main.temp}")

        // Paso 3: Probar deserialización con validación
        val validatedResult = deserializer.deserializeWithValidation(validJsonResponse)

        // 🔍 PUNTO DE DEBUG: Comparar resultados
        println("\n🛡️ Deserialización con validación:")
        println("   ¿Mismo nombre? ${basicResult.name == validatedResult.name}")
        println("   ¿Misma temp? ${basicResult.main.temp == validatedResult.main.temp}")

        // Paso 4: Conversión a dominio
        val domainResult = deserializer.deserializeToDomain(validJsonResponse)

        // 🔍 PUNTO DE DEBUG: Resultado final
        println("\n🏗️ Conversión a dominio:")
        println("   Tipo: ${domainResult::class.simpleName}")
        println("   Ciudad: ${domainResult.cityName}")
        println("   Descripción: ${domainResult.description}")

        println("\n🎯 Debug session completada!")

        assertNotNull(basicResult)
        assertNotNull(validatedResult)
        assertNotNull(domainResult)
    }

    @Test
    fun `test error messages are descriptive`() {
        println("🧪 Testing error message quality")

        val testCases = listOf(
            "{}" to "missing",
            """{"name": ""}""" to "empty",
            """{"name": "Test"}""" to "required"
        )

        testCases.forEach { (json, expectedKeyword) ->
            try {
                // 🔍 PUNTO DE DEBUG: Ver qué errores se generan
                deserializer.deserializeWithValidation(json)
                fail("Expected exception for: $json")
            } catch (e: Exception) {
                println("✅ Error para '$json': ${e.message}")
                assertTrue(
                    e.message?.contains(expectedKeyword, ignoreCase = true) == true,
                    "Error message should contain '$expectedKeyword': ${e.message}"
                )
            }
        }
    }
}