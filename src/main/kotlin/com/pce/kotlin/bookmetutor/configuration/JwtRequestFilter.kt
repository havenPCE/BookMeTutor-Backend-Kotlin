package com.pce.kotlin.bookmetutor.configuration

import com.pce.kotlin.bookmetutor.service.JwtTokenService
import com.pce.kotlin.bookmetutor.util.Constants
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
class JwtRequestFilter(@Qualifier("hybridDetailsService") private val userDetailsService: UserDetailsService,
                       private val jwtTokenService: JwtTokenService) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val tokenHeader: String? = request.getHeader(Constants.HEADER_FIELD)

        var userName: String? = null
        var token: String? = null

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7)
            userName = jwtTokenService.getUserNameFromToken(token)
        }
        if (userName != null && SecurityContextHolder.getContext().authentication == null && token != null) {
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(userName)
            if (jwtTokenService.validateToken(token, userDetails)) {
                val auth = UsernamePasswordAuthenticationToken(
                        userDetails.username,
                        userDetails.password,
                        userDetails.authorities
                )
                auth.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(request, response)
    }
}