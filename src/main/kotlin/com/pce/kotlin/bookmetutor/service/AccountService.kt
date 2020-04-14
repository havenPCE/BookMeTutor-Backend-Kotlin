package com.pce.kotlin.bookmetutor.service

interface AccountService {
    fun isPresent(email: String): Boolean
}