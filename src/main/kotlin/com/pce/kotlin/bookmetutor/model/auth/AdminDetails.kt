package com.pce.kotlin.bookmetutor.model.auth

import com.pce.kotlin.bookmetutor.model.dao.User
import com.pce.kotlin.bookmetutor.util.Authority
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AdminDetails(val admin: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(Authority.ADMIN.name))
    }

    override fun isEnabled(): Boolean {
        return admin.verified
    }

    override fun getUsername(): String {
        return admin.userName
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return admin.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }
}