package com.pce.kotlin.bookmetutor.model.dto.tutor

import java.time.LocalDateTime

data class UpdateTutorDto(
        val password: String?,
        val firstName: String?,
        val lastName: String?,
        val verified: Boolean?,
        val lastPicked: LocalDateTime?
)