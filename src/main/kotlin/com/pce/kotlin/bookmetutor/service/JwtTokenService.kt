package com.pce.kotlin.bookmetutor.service

import io.jsonwebtoken.Claims
import java.time.LocalDateTime

interface JwtTokenService {
    fun retrieveUserNameFromToken(token: String): String?

    fun retrieveExpirationDateFromToken(token: String): LocalDateTime?

    fun retrieveClaimsFromToken(token: String): Claims

    fun isTokenExpired(token: String): Boolean

    fun generate(subject: String): String

    fun validateToken(token: String): Boolean
}