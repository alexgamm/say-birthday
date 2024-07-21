package saybirthday.model

import java.time.MonthDay

data class ReminderInputState(
    var name: String? = null,
    var date: MonthDay? = null,
    val daysUntilBirthday: MutableSet<RemindDays> = mutableSetOf()
)