package saybirthday

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SayBirthdayApplication

fun main(args: Array<String>) {
    runApplication<SayBirthdayApplication>(*args)
}
