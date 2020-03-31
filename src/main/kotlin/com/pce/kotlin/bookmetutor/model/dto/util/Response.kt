package com.pce.kotlin.bookmetutor.model.dto.util

import org.springframework.http.HttpStatus

data class Response(val description: String, val status: HttpStatus, val data: Any?)