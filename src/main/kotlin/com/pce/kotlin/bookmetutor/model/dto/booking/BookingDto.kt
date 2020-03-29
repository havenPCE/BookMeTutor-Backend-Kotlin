package com.pce.kotlin.bookmetutor.model.dto.booking

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import java.time.LocalDateTime

data class BookingDto(
        val id: Long?,
        val subject: String,
        val topics: List<String>,
        val rejects: List<String>,
        val classNumber: Int,
        val board: String,
        val address: AddressDto?,
        val scheduleTime: LocalDateTime,
        val deadline: LocalDateTime,
        val startTime: LocalDateTime?,
        val endTime: LocalDateTime?,
        val rescheduled: Boolean,
        val secret: String,
        val score: Int,
        val comment: String?,
        val cancellationReason: String?,
        val reschedulingReason: String?,
        val status: String,
        val invoice: InvoiceDto?,
        val studentName: String?,
        val studentNumber: List<String>?,
        val studentEmail: String?,
        val tutorName: String?,
        val tutorNumber: List<String>?,
        val tutorEmail: String?
)