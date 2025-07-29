package data.core.deserializers

import kotlinx.serialization.json.*

/**
 * Deserializador base simplificado que proporciona funcionalidad común
 */
abstract class BaseDeserializer<DTO : Any, DOMAIN : Any> {

    // Configuración JSON común para todos los deserializadores
    protected val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        coerceInputValues = true
    }

    /**
     * Deserialización automática usando kotlinx.serialization
     */
    abstract fun deserialize(jsonString: String): DTO

    /**
     * Deserialización con validaciones personalizadas [Me gustaría definir el método de listas y objetos tmb]
     */
    abstract fun deserializeWithValidation(jsonString: String): DTO

    /**
     * Método helper para parsear JSON de forma segura
     */
    fun safeParseJson(jsonString: String): JsonObject? {
        return try {
            json.parseToJsonElement(jsonString).jsonObject
        } catch (e: Exception) {
            println("Error parsing JSON $e")
            null
        }
    }
}

/**
 * Excepción personalizada para errores de deserialización
 */
class DeserializationException(message: String, cause: Throwable? = null) : Exception(message, cause)