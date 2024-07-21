package saybirthday.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import saybirthday.entity.Reminder
import saybirthday.model.MatchedReminder
import saybirthday.utils.toDbColumn
import java.time.LocalDate

@Repository
class ReminderRepositoryImpl(private val db: JdbcTemplate) : ReminderRepository {
    override fun save(reminder: Reminder) {
        @Suppress("SqlNoDataSourceInspection")
        val sqlStatement = "INSERT INTO reminders (chat_id, name, birthday, days_until_birthday) VALUES (?, ?, ?, ?)"
        val daysUntilBirthday = db.dataSource!!.connection.createArrayOf(
                "integer",
                reminder.daysUntilBirthday.toTypedArray()
        )
        db.update(
                sqlStatement,
                reminder.chatId,
                reminder.name,
                reminder.birthday.toDbColumn(),
                daysUntilBirthday
        )
    }

    override fun findMatchedReminders(today: LocalDate): List<MatchedReminder> {
        """
        SELECT * FROM (
        SELECT id, chat_id, name, birthday, unnest(days_until_birthday) as days FROM reminders) as r
        WHERE ((:today)::date + r.days) = TO_DATE(birthday || '.' || EXTRACT(YEAR FROM (:today)::date)::text, 'DD.MM.YYYY');
        """
        TODO("Not yet implemented")
    }

}