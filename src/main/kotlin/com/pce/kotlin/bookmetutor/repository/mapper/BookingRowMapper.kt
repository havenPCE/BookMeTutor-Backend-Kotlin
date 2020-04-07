package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dao.BookingAddress
import com.pce.kotlin.bookmetutor.model.dao.Invoice
import com.pce.kotlin.bookmetutor.util.Board
import com.pce.kotlin.bookmetutor.util.BookingStatus
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
@Suppress("UNCHECKED_CAST")
class BookingRowMapper : RowMapper<Booking> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Booking? {
        return Booking(
                id = rs.getLong("booking_id"),
                board = Board.valueOf(rs.getString("board")),
                cancellationReason = rs.getString("cancellation_reason"),
                classNumber = rs.getInt("class_number"),
                rejects = (rs.getArray("rejects").array as Array<String>).toMutableSet(),
                rescheduled = rs.getBoolean("rescheduled"),
                reschedulingReason = rs.getString("rescheduling_reason"),
                comment = rs.getString("comment"),
                deadline = rs.getTimestamp("deadline"),
                scheduledTime = rs.getTimestamp("scheduled_time"),
                score = rs.getInt("score"),
                secret = rs.getString("score"),
                startTime = rs.getTimestamp("start_time"),
                endTime = rs.getTimestamp("end_time"),
                status = BookingStatus.valueOf(rs.getString("status")),
                subject = rs.getString("subject"),
                topics = (rs.getArray("topics").array as Array<String>).toMutableSet(),
                invoice = Invoice(
                        id = rs.getLong("invoice_id"),
                        amount = rs.getDouble("amount"),
                        method = PaymentMethod.valueOf(rs.getString("method")),
                        summary = rs.getString("summary")
                ),
                address = BookingAddress(
                        id = rs.getLong("address_id"),
                        line1 = rs.getString("line_1"),
                        line2 = rs.getString("line_2"),
                        landmark = rs.getString("landmark"),
                        city = rs.getString("city"),
                        pinCode = rs.getString("pin_code")
                )
        )
    }
}