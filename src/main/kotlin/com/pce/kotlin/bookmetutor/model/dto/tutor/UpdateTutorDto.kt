package com.pce.kotlin.bookmetutor.model.dto.tutor

import java.time.LocalDateTime

data class UpdateTutorDto(
        val password: String? = null,
        val firstName: String? = null,
        val lastName: String? = null,
        val verified: Boolean? = null,
        val lastPicked: LocalDateTime? = null
)