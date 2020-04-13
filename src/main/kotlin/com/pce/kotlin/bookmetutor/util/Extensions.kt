package com.pce.kotlin.bookmetutor.util

import com.pce.kotlin.bookmetutor.model.dto.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.random.Random

fun Random.nextString(length: Int = 10): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
            .map { nextInt(0, characters.length) }
            .map(characters::get)
            .joinToString("")
}

fun response(status: HttpStatus, description: String, payload: Any? = null) = ResponseEntity(Response(description = description, payload = payload), status)