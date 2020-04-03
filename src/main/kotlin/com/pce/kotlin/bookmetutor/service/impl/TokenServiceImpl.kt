package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.service.JwtTokenService
import com.pce.kotlin.bookmetutor.util.Constants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class TokenServiceImpl : JwtTokenService {
    override fun getUserNameFromToken(token: String): String? {
        return getClaimsFromToken(token).subject
    }

    override fun getExpirationDateFromToken(token: String): LocalDateTime? {
        return Instant.ofEpochMilli(getClaimsFromToken(token).expiration.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    override fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(token).body
    }

    override fun isTokenExpired(token: String): Boolean {
        return LocalDateTime.now().isAfter(getExpirationDateFromToken(token))
    }

    override fun generate(subject: String): String {
        return Jwts.builder()
                .setClaims(emptyMap<String, Any>())
                .setSubject(subject)
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(LocalDateTime.now().plusHours(Constants.EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.ES512, Constants.SECRET)
                .compact()
    }

    override fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return getUserNameFromToken(token) == userDetails.username && !isTokenExpired(token)
    }
}