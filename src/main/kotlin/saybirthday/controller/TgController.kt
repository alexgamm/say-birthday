package saybirthday.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.objects.Update
import saybirthday.service.TgService

@RestController
@RequestMapping("/tg")
class TgController(
    private val tgService: TgService
) {
    companion object {
        private val log = LoggerFactory.getLogger(TgController::class.java)
    }

    @ExceptionHandler
    fun handleException(ex: Throwable) {
        log.error("", ex)
    }

    @PostMapping("/webhook")
    fun handleWebhook(@RequestBody update: Update) {
        if (update.hasCallbackQuery()) {
            tgService.handleCallback(
                update.callbackQuery.message.chatId,
                update.callbackQuery.message.messageId,
                update.callbackQuery.data
            )
        } else if(update.message?.text != null) {
            tgService.handleMessage(update.message.chatId, update.message.text)
        }
    }
}