package saybirthday.utils

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import java.time.DateTimeException
import java.time.MonthDay

class DateUtilsTest : ShouldSpec({
    should("return MonthDay from d MMMM") {
        parse("5 февраля") shouldBe MonthDay.of(2, 5)
    }
    should("return MonthDay from d MM") {
        parse("5 02") shouldBe MonthDay.of(2, 5)
    }
    should("return MonthDay from d.MM") {
        parse("5.02") shouldBe MonthDay.of(2, 5)
    }
    should("return MonthDay from d/MM") {
        parse("05/02") shouldBe MonthDay.of(2, 5)
    }
    should("return MonthDay from d-MM") {
        parse("5-02") shouldBe MonthDay.of(2, 5)
    }
    should("return throw exception DateTimeException") {
        shouldThrow<DateTimeException> { parse("5-02-2023") }
    }
})