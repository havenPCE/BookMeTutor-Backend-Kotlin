package com.pce.kotlin.bookmetutor.model.dto.invoice

data class CreateInvoiceDto(
        val method: String,
        val amount: Double,
        val summary: String? = null
)