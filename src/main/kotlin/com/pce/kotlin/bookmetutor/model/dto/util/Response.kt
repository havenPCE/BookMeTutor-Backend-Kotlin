package com.pce.kotlin.bookmetutor.model.dto.util

import org.springframework.http.HttpStatus

data class Response(val message: String, val code: HttpStatus, val data: Any?)