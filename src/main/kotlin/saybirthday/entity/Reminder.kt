package saybirthday.entity

import java.time.MonthDay

class Reminder(
        var chatId: Long,
        var name: String,
        var birthday: MonthDay,
        var daysUntilBirthday: List<Int>,
        var id: Long? = null
)