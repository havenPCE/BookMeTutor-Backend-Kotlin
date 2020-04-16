package com.pce.kotlin.bookmetutor.model.dto.util

interface Response

data class PayloadResponse(val message: String, val payload: Any) : Response

data class EmptyResponse(val message: String) : Response