package com.pce.kotlin.bookmetutor.util

import com.pce.kotlin.bookmetutor.model.dto.util.EmptyResponse
import com.pce.kotlin.bookmetutor.model.dto.util.PayloadResponse
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

fun response(status: HttpStatus, message: String, payload: Any? = null) =
        payload?.let { ResponseEntity(PayloadResponse(message = message, payload = it), status) }
                ?: ResponseEntity(EmptyResponse(message = message), status)

fun String.h4(): String = "<h4>${this}</h4>"