package saybirthday.utils

import java.time.DateTimeException
import java.time.MonthDay
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

val FORMATTERS = listOf(
    "d MMMM",
    "d MM",
    "d.MM",
    "d/MM",
    "d-MM"
).map { pattern -> DateTimeFormatter.ofPattern(pattern, Locale("ru", "RU")) }

fun parse(date: String): MonthDay {
    FORMATTERS.forEach { formatter ->
        try {
            return MonthDay.parse(date, formatter)
        } catch (_: DateTimeParseException) {
        }
    }
    throw DateTimeException("Invalid date input: $date")
}