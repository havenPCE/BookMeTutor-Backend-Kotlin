package com.pce.kotlin.bookmetutor.service

import io.jsonwebtoken.Claims
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface JwtTokenService {
    fun retrieveUserNameFromToken(token: String): String?

    fun retrieveExpirationDateFromToken(token: String): Date?

    fun retrieveClaimsFromToken(token: String): Claims?

    fun isTokenExpired(token: String): Boolean

    fun generate(subject: String): String

    fun validateToken(token: String, userDetails: UserDetails): Boolean
}