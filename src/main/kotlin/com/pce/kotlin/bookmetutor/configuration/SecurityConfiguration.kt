package com.pce.kotlin.bookmetutor.configuration

import com.pce.kotlin.bookmetutor.util.Authority
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(@Qualifier("hybridDetailsService")
                            private val userDetailsService: UserDetailsService,
                            private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
                            private val jwtRequestFilter: JwtRequestFilter,
                            private val encoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/account/**", "/subject/**").permitAll()
                .antMatchers("/student/**").hasAnyAuthority(Authority.STUDENT.name, Authority.ADMIN.name)
                .antMatchers("/tutor/**").hasAnyAuthority(Authority.TUTOR.name, Authority.ADMIN.name)
                .antMatchers("/bmt-admin/**").hasAuthority(Authority.ADMIN.name)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)

    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder)
    }


    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}