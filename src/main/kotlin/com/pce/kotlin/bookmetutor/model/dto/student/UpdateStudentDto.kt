package com.pce.kotlin.bookmetutor.model.dto.student

data class UpdateStudentDto(
        val password: String?,
        val firstName: String?,
        val lastName: String?,
        val verified: Boolean?
)