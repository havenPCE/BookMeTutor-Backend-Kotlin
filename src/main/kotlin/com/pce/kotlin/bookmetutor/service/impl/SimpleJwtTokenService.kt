package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.service.JwtTokenService
import com.pce.kotlin.bookmetutor.util.EXPIRATION_TIME
import com.pce.kotlin.bookmetutor.util.SECRET
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class SimpleJwtTokenService : JwtTokenService {
    override fun retrieveUserNameFromToken(token: String): String? {
        return retrieveClaimsFromToken(token)?.subject
    }

    override fun retrieveExpirationDateFromToken(token: String): Date? {
        return retrieveClaimsFromToken(token)?.expiration
    }

    override fun retrieveClaimsFromToken(token: String): Claims? {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token)?.body
    }

    override fun isTokenExpired(token: String): Boolean {
        retrieveExpirationDateFromToken(token)?.let {
            return Date().after(it)
        }
        return true
    }

    override fun generate(subject: String): String {
        return Jwts.builder()
                .setClaims(mutableMapOf<String, Any>())
                .setSubject(subject)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact()
    }

    override fun validateToken(token: String, userDetails: UserDetails): Boolean {
        retrieveUserNameFromToken(token)?.let {
            return userDetails.username == it && !isTokenExpired(token)
        }
        return false
    }
}