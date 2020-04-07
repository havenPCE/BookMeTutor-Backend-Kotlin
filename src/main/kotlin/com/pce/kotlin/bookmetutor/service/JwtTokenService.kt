package com.pce.kotlin.bookmetutor.service

import io.jsonwebtoken.Claims
import java.time.LocalDateTime

interface JwtTokenService {
    fun getUserNameFromToken(token: String): String?

    fun getExpirationDateFromToken(token: String): LocalDateTime?

    fun getClaimsFromToken(token: String): Claims

    fun isTokenExpired(token: String): Boolean

    fun generate(subject: String): String

    fun validateToken(token: String): Boolean
}