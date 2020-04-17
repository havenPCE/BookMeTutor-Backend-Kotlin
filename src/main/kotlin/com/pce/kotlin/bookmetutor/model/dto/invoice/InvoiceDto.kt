package com.pce.kotlin.bookmetutor.model.dto.invoice

data class InvoiceDto(
        val id: Long,
        val method: String,
        val amount: Double,
        val summary: String? = null
)