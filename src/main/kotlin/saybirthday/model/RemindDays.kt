package saybirthday.model

enum class RemindDays(
    val buttonTitle: String,
    val fireTitle: String,
    val days: Int
) {
    SameDay("день в день", "Сегодня", 0),
    OneDay("за 1 день", "Завтра", 1),
    ThreeDays("3 дня", 3),
    OneWeek("неделю", 7),
    TwoWeeks("2 недели", 14),
    ThreeWeeks("3 недели", 21),
    Month("месяц", 30),
    TwoMonths("2 месяца", 60);

    constructor(period: String, days: Int) : this(
        "за $period",
        "Через $period",
        days
    )
}