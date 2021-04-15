package com.pce.kotlin.bookmetutor.configuration

import com.pce.kotlin.bookmetutor.service.JwtTokenService
import com.pce.kotlin.bookmetutor.util.HEADER_FIELD
import com.pce.kotlin.bookmetutor.util.TOKEN_PREFIX
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    @Qualifier("hybridDetailsService") val userDetailsService: UserDetailsService,
    val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val tokenHeader: String? = request.getHeader(HEADER_FIELD)
        val token: String? = tokenHeader.takeIf { it != null && it.startsWith(TOKEN_PREFIX) }?.substring(7)
        token?.let { t ->
            val userName: String? = jwtTokenService.retrieveUserNameFromToken(t)
            userName?.let { u ->
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(u)
                if (SecurityContextHolder.getContext().authentication == null && jwtTokenService.validateToken(
                        t,
                        userDetails
                    )
                ) {
                    val auth = UsernamePasswordAuthenticationToken(
                        userDetails.username,
                        userDetails.password,
                        userDetails.authorities
                    )
                    auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}