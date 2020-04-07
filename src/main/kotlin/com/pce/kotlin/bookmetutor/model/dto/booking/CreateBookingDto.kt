package com.pce.kotlin.bookmetutor.model.dto.booking

import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.CreateInvoiceDto
import java.util.*

data class CreateBookingDto(
        val subject: String,
        val topics: List<String>,
        val classNumber: Int,
        val board: String,
        val address: CreateAddressDto,
        val scheduledTime: Date,
        val invoice: CreateInvoiceDto
)