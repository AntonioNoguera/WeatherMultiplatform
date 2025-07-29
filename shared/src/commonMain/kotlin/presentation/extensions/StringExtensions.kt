package presentation.extensions

import kotlinx.datetime.*
import kotlin.native.concurrent.ThreadLocal

fun String.toUserFriendlyDate(): String {
    return try {
        // Convertimos "2025-07-09 18:00.00" a "2025-07-09T18:00:00"
        val cleaned = this.replace('.', ':')
        val (datePart, timePart) = cleaned.split(" ")
        val fullIso = "${datePart}T$timePart"

        val localDateTime = LocalDateTime.parse(fullIso)
        val instant = localDateTime.toInstant(TimeZone.UTC)
        val zoned = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val day = zoned.dayOfMonth
        val monthName = getSpanishMonthName(zoned.monthNumber)
        val year = zoned.year
        val hour12 = (zoned.hour % 12).let { if (it == 0) 12 else it }
        val minute = if (zoned.minute < 10) "0${zoned.minute}" else "${zoned.minute}"
        val ampm = if (zoned.hour < 12) "a.m." else "p.m"

        "$day de $monthName de $year, $hour12:$minute $ampm"
    } catch (e: Exception) {
        this // Si ocurre error, retorna el original
    }
}

private fun getSpanishMonthName(month: Int): String = when (month) {
    1 -> "enero"
    2 -> "febrero"
    3 -> "marzo"
    4 -> "abril"
    5 -> "mayo"
    6 -> "junio"
    7 -> "julio"
    8 -> "agosto"
    9 -> "septiembre"
    10 -> "octubre"
    11 -> "noviembre"
    12 -> "diciembre"
    else -> "mes desconocido"
}
