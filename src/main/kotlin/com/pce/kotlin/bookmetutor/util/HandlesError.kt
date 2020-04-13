package com.pce.kotlin.bookmetutor.util

import com.fasterxml.jackson.databind.JsonMappingException
import com.pce.kotlin.bookmetutor.model.dto.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

open class HandlesError {
    @ExceptionHandler(JsonMappingException::class)
    fun errorHandler(): ResponseEntity<out Response> {
        return response(status = HttpStatus.BAD_REQUEST, message = INVALID_REQUEST)
    }
}