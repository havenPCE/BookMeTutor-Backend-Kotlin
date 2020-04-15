package com.pce.kotlin.bookmetutor.model.dto.student

data class UpdateStudentDto(
        val password: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val verified: Boolean? = null
)