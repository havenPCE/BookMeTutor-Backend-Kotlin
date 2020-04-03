package com.pce.kotlin.bookmetutor.service

interface EmailService {
    fun sendMail(to: String, subject: String, text: String)
}