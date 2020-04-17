package com.pce.kotlin.bookmetutor.model.dto.booking

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import java.time.LocalDateTime

data class BookingDto(
        val id: Long? = null,
        val subject: String,
        val topics: List<String>,
        val rejects: List<String>,
        val classNumber: Int,
        val board: String,
        val address: AddressDto? = null,
        val scheduledTime: LocalDateTime,
        val deadline: LocalDateTime,
        val startTime: LocalDateTime? = null,
        val endTime: LocalDateTime? = null,
        val rescheduled: Boolean,
        val secret: String,
        val score: Int,
        val comment: String? = null,
        val cancellationReason: String? = null,
        val reschedulingReason: String? = null,
        val status: String,
        val invoice: InvoiceDto? = null,
        val studentEmail: String? = null,
        val tutorEmail: String? = null,
        val studentPhone: String? = null,
        val tutorPhone: String? = null
)