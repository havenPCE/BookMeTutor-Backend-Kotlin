package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.invoice.CreateInvoiceDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import kotlin.random.Random

data class Invoice(
    val id: Long = Random.nextLong(Long.MAX_VALUE),
    val amount: Double,
    val method: PaymentMethod,
    val summary: String? = null,
    val bookingId: Long? = null
) {
    fun toDto() = InvoiceDto(
        id = this.id,
        amount = this.amount,
        method = this.method.name,
        summary = this.summary
    )

    companion object {
        fun fromDto(dto: CreateInvoiceDto) = Invoice(
            amount = dto.amount,
            method = PaymentMethod.valueOf(dto.method),
            summary = dto.summary
        )
    }
}
