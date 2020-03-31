package com.pce.kotlin.bookmetutor.model.dto.booking

import java.time.LocalDateTime

data class UpdateBookingDto(
        val topics: List<String>?,
        val scheduleTime: LocalDateTime?,
        val startTime: LocalDateTime?,
        val endTime: LocalDateTime?,
        val rescheduled: Boolean?,
        val score: Int?,
        val comment: String?,
        val cancellationReason: String?,
        val reschedulingReason: String?,
        val status: String?,
        val reject: String?
)