package saybirthday.repository

import saybirthday.entity.Reminder
import saybirthday.model.MatchedReminder
import java.time.LocalDate

interface ReminderRepository {
    fun save(reminder: Reminder)
    fun findMatchedReminders(today: LocalDate): List<MatchedReminder>
}