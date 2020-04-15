package com.pce.kotlin.bookmetutor.model.dto.student

data class CreateStudentDto(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String? = null,
        val gender: String,
        val phone: String
)