package com.pce.kotlin.bookmetutor.model.dto.booking

import java.time.LocalDateTime


data class UpdateBookingDto(
        val topics: List<String>? = null,
        val scheduleTime: LocalDateTime? = null,
        val startTime: LocalDateTime? = null,
        val endTime: LocalDateTime? = null,
        val rescheduled: Boolean? = null,
        val score: Int? = null,
        val comment: String? = null,
        val cancellationReason: String? = null,
        val reschedulingReason: String? = null,
        val status: String? = null,
        val reject: String? = null
)