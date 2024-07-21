package saybirthday.service

import saybirthday.model.ReminderInputState

interface TgService {
    val inputStates: MutableMap<Long, ReminderInputState>
    fun handleMessage(chatId: Long, text: String)
    fun handleCallback(chatId: Long, messageId: Int, callbackData: String)
    fun sendMessage(chatId: Long, text: String)
}