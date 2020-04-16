package com.pce.kotlin.bookmetutor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class BookmetutorApplication

fun main(args: Array<String>) {
    runApplication<BookmetutorApplication>(*args)
}
