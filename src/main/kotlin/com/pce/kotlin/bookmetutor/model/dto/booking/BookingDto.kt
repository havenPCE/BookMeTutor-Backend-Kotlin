package com.pce.kotlin.bookmetutor.model.dto.booking

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import java.util.*

data class BookingDto(
        val id: Long?,
        val subject: String,
        val topics: List<String>,
        val rejects: List<String>,
        val classNumber: Int,
        val board: String,
        val address: AddressDto?,
        val scheduleTime: Date,
        val deadline: Date,
        val startTime: Date?,
        val endTime: Date?,
        val rescheduled: Boolean,
        val secret: String,
        val score: Int,
        val comment: String?,
        val cancellationReason: String?,
        val reschedulingReason: String?,
        val status: String,
        val invoice: InvoiceDto?
)