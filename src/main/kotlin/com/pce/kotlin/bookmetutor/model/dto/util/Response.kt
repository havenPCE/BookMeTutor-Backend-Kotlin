package com.pce.kotlin.bookmetutor.model.dto.util

import java.time.LocalDateTime

interface Response

data class PayloadResponse(val message: String, val timestamp: LocalDateTime = LocalDateTime.now(), val payload: Any) : Response

data class EmptyResponse(val message: String, val timestamp: LocalDateTime = LocalDateTime.now()) : Response