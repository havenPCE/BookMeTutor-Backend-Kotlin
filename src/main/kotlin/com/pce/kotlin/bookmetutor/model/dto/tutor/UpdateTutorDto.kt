package com.pce.kotlin.bookmetutor.model.dto.tutor

import java.util.*

data class UpdateTutorDto(
        val password: String?,
        val firstName: String?,
        val lastName: String?,
        val verified: Boolean?,
        val lastPicked: Date?
)