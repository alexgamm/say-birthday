//package saybirthday.service
//
//import com.ninjasquad.springmockk.MockkBean
//import io.kotest.matchers.nulls.shouldNotBeNull
//import io.kotest.matchers.shouldBe
//import io.mockk.verify
//import org.junit.jupiter.api.Test
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
//import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
//import org.telegram.telegrambots.meta.generics.TelegramClient
//import saybirthday.common.chooseReminder
//import saybirthday.common.enterReminderName
//import saybirthday.common.reminderApproved
//import saybirthday.common.reminderNameAcceptedEnterBirthdayDate
//import saybirthday.model.RemindDays
//import saybirthday.repository.ReminderRepository
//import saybirthday.service.impl.SAVE_REMINDER
//import java.time.MonthDay
//
//@DataJpaTest
//class TgServiceTest @Autowired constructor (
//    val service: TgService,
//    val reminderRepository: ReminderRepository
//) {
//
//    @MockkBean(relaxed = true)
//    lateinit var tgClient: TelegramClient
//
//    @Test
//    fun testSaveReminder() {
//        val chatId = 1L
//        val multiselectMessageId = 1
//        val name = "Саша"
//        service.handleMessage(chatId, "/addreminder")
//        verify {
//            tgClient.execute(match<SendMessage> {
//                it.chatId == chatId.toString() && it.text == enterReminderName()
//            })
//        }
//        service.inputStates[chatId].shouldNotBeNull()
//
//        service.handleMessage(chatId, name)
//        verify {
//            tgClient.execute(match<SendMessage> {
//                it.chatId == chatId.toString() && it.text == reminderNameAcceptedEnterBirthdayDate(name)
//            })
//        }
//        service.inputStates[chatId]!!.name shouldBe name
//
//        service.handleMessage(chatId, "11 февраля")
//        verify {
//            tgClient.execute(match<SendMessage> {
//                it.chatId == chatId.toString()
//                        && it.text == chooseReminder()
//                        && (it.replyMarkup as InlineKeyboardMarkup).keyboard.let { keyboard ->
//                    keyboard[0][0].text == RemindDays.SameDay.buttonTitle
//                            && keyboard[0][0].callbackData == RemindDays.SameDay.name
//                            && keyboard[1][0].text == RemindDays.OneDay.buttonTitle
//                            && keyboard[1][0].callbackData == RemindDays.OneDay.name
//                            && keyboard[2][0].text == RemindDays.ThreeDays.buttonTitle
//                            && keyboard[2][0].callbackData == RemindDays.ThreeDays.name
//                            && keyboard[3][0].text == RemindDays.OneWeek.buttonTitle
//                            && keyboard[3][0].callbackData == RemindDays.OneWeek.name
//                            && keyboard[4][0].text == RemindDays.TwoWeeks.buttonTitle
//                            && keyboard[4][0].callbackData == RemindDays.TwoWeeks.name
//                            && keyboard[5][0].text == RemindDays.ThreeWeeks.buttonTitle
//                            && keyboard[5][0].callbackData == RemindDays.ThreeWeeks.name
//                            && keyboard[6][0].text == RemindDays.Month.buttonTitle
//                            && keyboard[6][0].callbackData == RemindDays.Month.name
//                            && keyboard[7][0].text == RemindDays.TwoMonths.buttonTitle
//                            && keyboard[7][0].callbackData == RemindDays.TwoMonths.name
//                }
//            })
//        }
//        service.inputStates[chatId]!!.date shouldBe MonthDay.of(2, 11)
//
//        service.handleCallback(chatId, multiselectMessageId, RemindDays.TwoMonths.name)
//        verify {
//            tgClient.execute(match<EditMessageReplyMarkup> {
//                it.chatId == chatId.toString()
//                        && it.messageId == multiselectMessageId
//                        && (it.replyMarkup as InlineKeyboardMarkup).keyboard.let { keyboard ->
//                    keyboard[0][0].text == RemindDays.SameDay.buttonTitle
//                            && keyboard[0][0].callbackData == RemindDays.SameDay.name
//                            && keyboard[1][0].text == RemindDays.OneDay.buttonTitle
//                            && keyboard[1][0].callbackData == RemindDays.OneDay.name
//                            && keyboard[2][0].text == RemindDays.ThreeDays.buttonTitle
//                            && keyboard[2][0].callbackData == RemindDays.ThreeDays.name
//                            && keyboard[3][0].text == RemindDays.OneWeek.buttonTitle
//                            && keyboard[3][0].callbackData == RemindDays.OneWeek.name
//                            && keyboard[4][0].text == RemindDays.TwoWeeks.buttonTitle
//                            && keyboard[4][0].callbackData == RemindDays.TwoWeeks.name
//                            && keyboard[5][0].text == RemindDays.ThreeWeeks.buttonTitle
//                            && keyboard[5][0].callbackData == RemindDays.ThreeWeeks.name
//                            && keyboard[6][0].text == RemindDays.Month.buttonTitle
//                            && keyboard[6][0].callbackData == RemindDays.Month.name
//                            && keyboard[7][0].text == "✓ ${RemindDays.TwoMonths.buttonTitle}"
//                            && keyboard[7][0].callbackData == RemindDays.TwoMonths.name
//                            && keyboard[8][0].text == "Сохранить выбор"
//                            && keyboard[8][0].callbackData == SAVE_REMINDER
//                }
//            })
//        }
//        service.inputStates[chatId]!!.daysUntilBirthday shouldBe mutableSetOf(RemindDays.TwoMonths)
//
//        service.handleCallback(chatId, multiselectMessageId, SAVE_REMINDER)
//        verify {
//            tgClient.execute(match<EditMessageText> {
//                it.chatId == chatId.toString()
//                        && it.text == reminderApproved(name, service.inputStates[chatId]!!.daysUntilBirthday)
//            })
//        }
//
//    }
//}