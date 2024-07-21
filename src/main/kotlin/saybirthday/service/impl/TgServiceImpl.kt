package saybirthday.service.impl

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import org.telegram.telegrambots.meta.generics.TelegramClient
import saybirthday.common.*
import saybirthday.controller.TgController
import saybirthday.entity.Reminder
import saybirthday.model.RemindDays
import saybirthday.model.ReminderInputState
import saybirthday.repository.ReminderRepository
import saybirthday.service.TgService
import saybirthday.utils.parse
import java.time.DateTimeException

const val SAVE_REMINDER = "saveReminder"

@Service
class TgServiceImpl(
    private val tgClient: TelegramClient,
    private val reminderRepository: ReminderRepository
) : TgService {
    companion object {
        private val log = LoggerFactory.getLogger(TgController::class.java);
    }

    override val inputStates: MutableMap<Long, ReminderInputState> = mutableMapOf()

    override fun sendMessage(chatId: Long, text: String) {
        tgClient.execute(
            SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode(ParseMode.MARKDOWNV2)
                .build()
        )
    }

    override fun handleMessage(chatId: Long, text: String) {
        when (text) {
            "/start" -> {
                log.info("User {} started to use bot", chatId)
                sendMessage(chatId, welcome())
            }

            "/addreminder" -> {
                sendMessage(chatId, enterReminderName())
                inputStates[chatId] = ReminderInputState()
            }

            else -> {
                val inputState = inputStates[chatId]
                if (inputState == null || inputState.date != null) {
                    sendMessage(chatId, unknownCommand())
                    return
                }
                if (inputState.name == null) {
                    inputState.name = text
                    sendMessage(chatId, reminderNameAcceptedEnterBirthdayDate(inputState.name!!))
                    return
                }

                try {
                    inputState.date = parse(text)
                } catch (_: DateTimeException) {
                    sendMessage(chatId, invalidDateFormat())
                    return
                }
                tgClient.execute(
                    SendMessage.builder()
                        .chatId(chatId)
                        .text(chooseReminder())
                        .replyMarkup(buildReplyMarkup())
                        .build()
                )
            }
        }
    }

    override fun handleCallback(chatId: Long, messageId: Int, callbackData: String) {
        val inputState = inputStates[chatId] ?: return
        if (callbackData == SAVE_REMINDER) {
            reminderRepository.save(
                Reminder(
                    chatId = chatId,
                    name = inputState.name!!,
                    birthday = inputState.date!!,
                    daysUntilBirthday = inputState.daysUntilBirthday.map { it.days }
                )
            )
            tgClient.execute(DeleteMessage.builder().chatId(chatId).messageId(messageId).build())
            tgClient.execute(
                SendMessage.builder()
                    .chatId(chatId)
                    .text(reminderApproved(inputState.name!!, inputState.daysUntilBirthday))
                    .build()
            )
            return
        }
        val chosenDay = RemindDays.valueOf(callbackData)
        if (inputState.daysUntilBirthday.contains(chosenDay)) {
            inputState.daysUntilBirthday.remove(chosenDay)
        } else {
            inputState.daysUntilBirthday.add(chosenDay)
        }
        tgClient.execute(
            EditMessageReplyMarkup.builder()
                .messageId(messageId)
                .chatId(chatId)
                .replyMarkup(buildReplyMarkup(inputState.daysUntilBirthday))
                .build()
        )
    }

    fun buildReplyMarkup(chosenButtons: Set<RemindDays> = emptySet()): InlineKeyboardMarkup {
        val rows = RemindDays.entries.map { remindDays ->
            var title = remindDays.buttonTitle
            if (chosenButtons.contains(remindDays)) {
                title = "✓ $title"
            }
            InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text(title)
                    .callbackData(remindDays.name)
                    .build()
            )
        }.toMutableList()
        if (chosenButtons.isNotEmpty()) {
            rows += InlineKeyboardRow(
                InlineKeyboardButton.builder()
                    .text("Сохранить выбор")
                    .callbackData(SAVE_REMINDER)
                    .build()
            )
        }
        return InlineKeyboardMarkup.builder()
            .keyboard(rows)
            .build()
    }

}