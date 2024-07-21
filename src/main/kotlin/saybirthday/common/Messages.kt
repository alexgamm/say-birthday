package saybirthday.common

import saybirthday.model.RemindDays

fun welcome() = """
    |Добро пожаловать!
    |Чтобы установить напоминание о дне рождения, введи /addreminder""".trimMargin()

fun enterReminderName() = "Введи имя для напоминания"
fun unknownCommand() = "Неизвестная команда"
fun reminderNameAcceptedEnterBirthdayDate(name: String) = """
    |Имя *$name* успешно задано\.
    |Теперь нужно ввести дату рождения \(день месяц\)""".trimMargin()

fun invalidDateFormat() = """
    |Неправильный формат даты\.
    |Необходимо ввести дату рождения \(например: 5 февраля\)""".trimMargin()

fun reminderApproved(name: String, remindDays: Set<RemindDays>): String {
    val remindDaysText = remindDays.sorted().joinToString(", ") { it.buttonTitle }
    return "Теперь я буду напонимать тебе о дне рождения *$name* $remindDaysText"
}

fun chooseReminder() = "Выбери, за сколько напоминать о дне рождения"
fun remind(name: String, remindDays: RemindDays) = "${remindDays.fireTitle} день рождения у *$name* 🎉"
