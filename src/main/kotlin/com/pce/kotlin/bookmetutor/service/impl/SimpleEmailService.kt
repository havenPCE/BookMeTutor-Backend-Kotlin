package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.service.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import javax.mail.internet.MimeMessage

@Service
class SimpleEmailService(private val javaMailSender: JavaMailSender) : EmailService {
    override fun sendMail(to: String, subject: String, text: String) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        helper.apply {
            setTo(to)
            setSubject(to)
            setText(text, true)
        }
        javaMailSender.send(mimeMessage)
    }
}