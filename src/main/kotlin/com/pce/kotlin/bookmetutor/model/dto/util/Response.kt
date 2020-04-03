package com.pce.kotlin.bookmetutor.model.dto.util

data class Response(val description: String, val payload: Any?) {
    constructor(description: String) : this(description, null)
}