package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Invoice
import com.pce.kotlin.bookmetutor.repository.InvoiceRepo
import com.pce.kotlin.bookmetutor.util.InvoiceQuery
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcInvoiceRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : InvoiceRepo {

    val invoiceRowMapper: (ResultSet, Int) -> Invoice = { rs, _ ->
        Invoice(
            id = rs.getLong("invoice_id"),
            amount = rs.getDouble("amount"),
            method = PaymentMethod.valueOf(rs.getString("method")),
            summary = rs.getString("summary"),
            bookingId = rs.getLong("booking_id")
        )
    }

    override fun findById(id: Long): Invoice? {
        val (selectQuery, selectParams) = InvoiceQuery.selectById(id)
        return jdbcTemplate.query(selectQuery, selectParams, invoiceRowMapper).firstOrNull()
    }

    override fun save(bookingId: Long, invoice: Invoice): Invoice? {
        val (insertQuery, insertParams) = InvoiceQuery.insert(bookingId, invoice)
        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findById(invoice.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(invoice: Invoice): Invoice? {
        val (updateQuery, updateParams) = InvoiceQuery.update(invoice)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findById(invoice.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = InvoiceQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findByBookingId(bookingId: Long): Invoice? {
        val (selectQuery, selectParams) = InvoiceQuery.selectByBookingId(bookingId)
        return jdbcTemplate.query(selectQuery, selectParams, invoiceRowMapper).firstOrNull()
    }
}