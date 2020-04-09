package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Invoice

interface InvoiceRepo {
    fun findById(id: Long): Invoice?
    fun save(bookingId: Long, invoice: Invoice): Invoice?
    fun update(invoice: Invoice): Invoice?
    fun deleteById(id: Long): Boolean
    fun findByBookingId(bookingId: Long): Invoice?
}