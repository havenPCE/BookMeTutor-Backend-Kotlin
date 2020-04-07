package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Invoice
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class InvoiceRowMapper : RowMapper<Invoice> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Invoice? {
        return Invoice(
                id = rs.getLong("invoice_id"),
                amount = rs.getDouble("amount"),
                method = PaymentMethod.valueOf(rs.getString("method")),
                summary = rs.getString("summary")
        )
    }
}