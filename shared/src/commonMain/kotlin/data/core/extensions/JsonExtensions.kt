package data.core.extensions

import data.core.deserializers.DeserializationException
import kotlinx.serialization.json.*

/**
 * Extension functions para facilitar el trabajo con JsonObject
 */

// String operations
fun JsonObject.getStringOrNull(key: String): String? {
    return this[key]?.jsonPrimitive?.contentOrNull
}

fun JsonObject.getRequiredString(key: String, fieldName: String = key): String {
    return getStringOrNull(key)
        ?: throw DeserializationException("$fieldName! is required but was missing or null")
}

fun String.validateNotEmpty(fieldName: String): String {
    return this.takeIf { it.isNotBlank() }
        ?: throw DeserializationException("$fieldName cannot be empty")
}

// Double operations
fun JsonObject.getDoubleOrNull(key: String): Double? {
    return this[key]?.jsonPrimitive?.doubleOrNull
}

fun JsonObject.getRequiredDouble(key: String, fieldName: String = key): Double {
    return getDoubleOrNull(key)
        ?: throw DeserializationException("$fieldName is required but was missing or invalid")
}

fun Double.validateInRange(min: Double, max: Double, fieldName: String): Double {
    return this.coerceIn(min, max).also {
        if (it != this) {
            println("WARNING: $fieldName value $this was clamped to $it (range: $min-$max)")
        }
    }
}

fun Double.validatePositive(fieldName: String): Double {
    return if (this < 0) {
        println("WARNING: $fieldName value $this was negative, converted to 0.0")
        0.0
    } else this
}

// Int operations
fun JsonObject.getIntOrNull(key: String): Int? {
    return this[key]?.jsonPrimitive?.intOrNull
}

fun JsonObject.getRequiredInt(key: String, fieldName: String = key): Int {
    return getIntOrNull(key)
        ?: throw DeserializationException("$fieldName is required but was missing or invalid")
}

fun Int.validateInRange(min: Int, max: Int, fieldName: String): Int {
    return this.coerceIn(min, max).also {
        if (it != this) {
            println("WARNING: $fieldName value $this was clamped to $it (range: $min-$max)")
        }
    }
}

fun Int.validatePositive(fieldName: String): Int {
    return if (this < 0) {
        println("WARNING: $fieldName value $this was negative, converted to 0")
        0
    } else this
}

// JsonObject operations
fun JsonObject.getObjectOrNull(key: String): JsonObject? {
    return this[key]?.let { element ->
        if (element is JsonObject) element else null
    }
}

fun JsonObject.getRequiredObject(key: String, fieldName: String = key): JsonObject {
    return getObjectOrNull(key)
        ?: throw DeserializationException("$fieldName object is required but was missing")
}

// JsonArray operations
fun JsonObject.getArrayOrNull(key: String): JsonArray? {
    return this[key]?.let { element ->
        if (element is JsonArray) element else null
    }
}

fun JsonObject.getRequiredArray(key: String, fieldName: String = key): JsonArray {
    return getArrayOrNull(key)
        ?: throw DeserializationException("$fieldName array is required but was missing")
}
