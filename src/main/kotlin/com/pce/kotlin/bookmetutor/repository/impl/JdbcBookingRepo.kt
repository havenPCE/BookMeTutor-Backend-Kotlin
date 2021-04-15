package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.util.Board
import com.pce.kotlin.bookmetutor.util.BookingQuery
import com.pce.kotlin.bookmetutor.util.BookingStatus
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcBookingRepo(
    val jdbcTemplate: NamedParameterJdbcTemplate,
    val invoiceRepo: JdbcInvoiceRepo,
    val bookingAddressRepo: JdbcBookingAddressRepo
) : BookingRepo {

    val bookingIdRowMapper: (ResultSet, Int) -> Long = { rs, _ -> rs.getLong("booking_id") }
    val bookingRowMapper: (ResultSet, Int) -> Booking = { rs, _ ->
        Booking(
            id = rs.getLong("booking_id"),
            board = Board.valueOf(rs.getString("board")),
            cancellationReason = rs.getString("cancellation_reason"),
            classNumber = rs.getInt("class_number"),
            rejects = emptySet(),
            rescheduled = rs.getBoolean("rescheduled"),
            reschedulingReason = rs.getString("rescheduling_reason"),
            comment = rs.getString("comment"),
            deadline = rs.getTimestamp("deadline").toLocalDateTime(),
            scheduledTime = rs.getTimestamp("scheduled_time").toLocalDateTime(),
            score = rs.getInt("score"),
            secret = rs.getString("secret"),
            startTime = rs.getTimestamp("start_time")?.toLocalDateTime(),
            endTime = rs.getTimestamp("end_time")?.toLocalDateTime(),
            status = BookingStatus.valueOf(rs.getString("status")),
            subject = rs.getString("subject"),
            topics = emptySet(),
            studentId = rs.getLong("student_id"),
            tutorId = rs.getLong("tutor_id")
        )
    }
    val bookingRejectRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("reject") }
    val bookingTopicRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("topic") }

    override fun findById(id: Long): Booking? {
        val (selectBookingQuery, selectBookingParams) = BookingQuery.selectById(id)
        val (selectBookingRejectQuery, selectBookingRejectParams) = BookingQuery.selectReject(id)
        val (selectBookingTopicQuery, selectBookingTopicParams) = BookingQuery.selectTopic(id)
        var booking: Booking? =
            jdbcTemplate.query(selectBookingQuery, selectBookingParams, bookingRowMapper).firstOrNull()
        booking = booking?.copy(
            rejects = jdbcTemplate.query(selectBookingRejectQuery, selectBookingRejectParams, bookingRejectRowMapper)
                .toSet(),
            topics = jdbcTemplate.query(selectBookingTopicQuery, selectBookingTopicParams, bookingTopicRowMapper)
                .toSet(),
            invoice = invoiceRepo.findByBookingId(booking.id),
            address = bookingAddressRepo.findByBookingId(booking.id)
        )
        return booking
    }

    override fun save(studentId: Long, tutorId: Long, booking: Booking): Booking? {
        val (insertBookingQuery, insertBookingParams) = BookingQuery.insertIntoBooking(studentId, tutorId, booking)
        val insertRejectQuery = BookingQuery.insertIntoReject(booking.id)
        val insertRejectParams = createRejectParams(booking.rejects)
        val insertTopicQuery = BookingQuery.insertIntoTopic(booking.id)
        val insertTopicParams = createTopicParams(booking.topics)
        return try {
            jdbcTemplate.update(insertBookingQuery, insertBookingParams)
            jdbcTemplate.batchUpdate(insertRejectQuery, insertRejectParams)
            jdbcTemplate.batchUpdate(insertTopicQuery, insertTopicParams)
            booking.address?.let { bookingAddressRepo.save(booking.id, it) }
            booking.invoice?.let { invoiceRepo.save(booking.id, it) }
            findById(booking.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(booking: Booking, tutorId: Long?): Booking? {
        val (updateBookingQuery, updateBookingParams) = tutorId?.let {
            BookingQuery.updateBookingWithTutor(
                it,
                booking
            )
        }
            ?: BookingQuery.updateBooking(booking)
        val (deleteTopicQuery, deleteTopicParams) = BookingQuery.deleteFromTopic(booking.id)
        val (deleteRejectQuery, deleteRejectParams) = BookingQuery.deleteFromReject(booking.id)
        val insertRejectQuery = BookingQuery.insertIntoReject(booking.id)
        val insertRejectParams = createRejectParams(booking.rejects)
        val insertTopicQuery = BookingQuery.insertIntoTopic(booking.id)
        val insertTopicParams = createTopicParams(booking.topics)
        return try {
            jdbcTemplate.update(updateBookingQuery, updateBookingParams)
            jdbcTemplate.update(deleteRejectQuery, deleteRejectParams)
            jdbcTemplate.batchUpdate(insertRejectQuery, insertRejectParams)
            jdbcTemplate.update(deleteTopicQuery, deleteTopicParams)
            jdbcTemplate.batchUpdate(insertTopicQuery, insertTopicParams)
            booking.address?.let { bookingAddressRepo.update(it) }
            booking.invoice?.let { invoiceRepo.update(it) }
            findById(booking.id)
        } catch (e: java.lang.Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = BookingQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findAll(): List<Booking> {
        val selectQuery = """SELECT booking_id FROM public.booking;"""
        return jdbcTemplate.query(selectQuery, bookingIdRowMapper)
            .mapNotNull { findById(it) }
    }

    override fun findByStudentId(studentId: Long): List<Booking> {
        val (selectQuery, selectParams) = BookingQuery.selectByStudentId(studentId)
        return jdbcTemplate.query(selectQuery, selectParams, bookingIdRowMapper)
            .mapNotNull { findById(it) }
    }

    override fun findByTutorId(tutorId: Long): List<Booking> {
        val (selectQuery, selectParams) = BookingQuery.selectByTutorId(tutorId)
        return jdbcTemplate.query(selectQuery, selectParams, bookingIdRowMapper)
            .mapNotNull { findById(it) }
    }

    fun createRejectParams(rejects: Collection<String>): Array<SqlParameterSource> =
        SqlParameterSourceUtils.createBatch(rejects.map { mutableMapOf("reject" to it) }.toTypedArray())

    fun createTopicParams(topics: Collection<String>): Array<SqlParameterSource> =
        SqlParameterSourceUtils.createBatch(topics.map { mutableMapOf("topic" to it) }.toTypedArray())
}