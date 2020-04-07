package com.pce.kotlin.bookmetutor.model.dto.booking

import java.util.*


data class UpdateBookingDto(
        val topics: List<String>?,
        val scheduleTime: Date?,
        val startTime: Date?,
        val endTime: Date?,
        val rescheduled: Boolean?,
        val score: Int?,
        val comment: String?,
        val cancellationReason: String?,
        val reschedulingReason: String?,
        val status: String?,
        val reject: String?
)