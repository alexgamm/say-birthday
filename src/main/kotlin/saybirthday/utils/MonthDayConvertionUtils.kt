package saybirthday.utils

import java.time.MonthDay
import java.time.format.DateTimeFormatter


private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM")
fun MonthDay.toDbColumn() = format(FORMATTER)

fun fromDbColumn(str: String) = MonthDay.parse(str, FORMATTER)
