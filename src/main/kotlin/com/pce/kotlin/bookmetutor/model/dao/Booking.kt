package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.util.Board
import com.pce.kotlin.bookmetutor.util.BookingStatus
import com.pce.kotlin.bookmetutor.util.Constants
import com.pce.kotlin.bookmetutor.util.nextString
import java.util.*
import kotlin.random.Random

data class Booking(
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val board: Board,
        val cancellationReason: String? = null,
        val classNumber: Int,
        val rejects: Set<String> = mutableSetOf(),
        val rescheduled: Boolean = false,
        val reschedulingReason: String? = null,
        val comment: String? = null,
        val deadline: Date,
        val scheduledTime: Date,
        val score: Int = 0,
        val secret: String = Random.nextString(length = Constants.SECRET_OTP_LENGTH),
        val startTime: Date? = null,
        val endTime: Date? = null,
        val status: BookingStatus = BookingStatus.PENDING,
        val subject: String,
        val topics: Set<String> = mutableSetOf(),
        val invoice: Invoice? = null,
        val address: BookingAddress? = null
) {
    fun toDto() = BookingDto(
            id = this.id,
            board = this.board.name,
            cancellationReason = this.cancellationReason,
            classNumber = this.classNumber,
            comment = this.comment,
            deadline = this.deadline,
            scheduleTime = this.scheduledTime,
            score = this.score,
            startTime = this.startTime,
            endTime = this.endTime,
            status = this.status.name,
            subject = this.subject,
            topics = this.topics.toList(),
            invoice = this.invoice?.toDto(),
            address = this.address?.toDto(),
            secret = this.secret,
            rejects = this.rejects.toList(),
            rescheduled = this.rescheduled,
            reschedulingReason = this.reschedulingReason
    )

    companion object {
        fun fromDto(dto: CreateBookingDto) = Booking(
                board = Board.valueOf(dto.board),
                subject = dto.subject,
                topics = dto.topics.toSet(),
                classNumber = dto.classNumber,
                address = BookingAddress.fromDto(dto.address),
                scheduledTime = dto.scheduledTime,
                deadline = Date(dto.scheduledTime.time - Constants.DEADLINE_HOURS),
                invoice = Invoice.fromDto(dto.invoice)
        )

        fun fromDto(dto: UpdateBookingDto, default: Booking) = default.copy(
                topics = dto.topics?.toSet() ?: default.topics,
                scheduledTime = dto.scheduleTime ?: default.scheduledTime,
                startTime = dto.startTime ?: default.startTime,
                endTime = dto.endTime ?: default.endTime,
                rescheduled = dto.rescheduled ?: default.rescheduled,
                score = dto.score ?: default.score,
                comment = dto.comment ?: default.comment,
                cancellationReason = dto.cancellationReason ?: default.cancellationReason,
                reschedulingReason = dto.reschedulingReason ?: default.reschedulingReason,
                status = dto.status?.let { BookingStatus.valueOf(it) } ?: default.status,
                rejects = dto.reject?.let { default.rejects.plus(it) } ?: default.rejects
        )
    }
}
