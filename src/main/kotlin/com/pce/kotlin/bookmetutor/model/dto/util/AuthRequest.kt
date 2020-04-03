package com.pce.kotlin.bookmetutor.model.dto.util

data class AuthRequest(
        val email: String,
        val password: String,
        val role: String
)
