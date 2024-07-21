package saybirthday.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import saybirthday.model.RemindDays
import saybirthday.repository.ReminderRepository
import saybirthday.service.TgService
import java.time.LocalDate

@RestController
@RequestMapping("/reminder")
class ReminderController(
    private val tgService: TgService,
    private val reminderRepository: ReminderRepository
) {

    companion object {
        private val log = LoggerFactory.getLogger(ReminderController::class.java)
    }

    @PostMapping("/remind")
    fun remind() {
        reminderRepository.findMatchedReminders(LocalDate.now()).forEach { reminder ->
            try {
                tgService.sendMessage(
                    reminder.chatId,
                    saybirthday.common.remind(reminder.name, RemindDays.entries.first { it.days == reminder.days })
                )
            } catch (e: Exception) {
                log.warn("Could not send reminder {}", reminder, e)
            }
        }
    }
}