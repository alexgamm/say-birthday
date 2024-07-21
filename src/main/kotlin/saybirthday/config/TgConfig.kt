package saybirthday.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient

@Configuration
class TgConfig {

    @Bean
    fun telegramClient(@Value("\${tg.bot.token}") token: String) = OkHttpTelegramClient(token)

}