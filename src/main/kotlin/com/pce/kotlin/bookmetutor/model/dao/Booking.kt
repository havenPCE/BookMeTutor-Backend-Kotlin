package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.util.*
import java.time.LocalDateTime
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
        val deadline: LocalDateTime,
        val scheduledTime: LocalDateTime,
        val score: Int = 0,
        val secret: String = Random.nextString(length = SECRET_OTP_LENGTH),
        val startTime: LocalDateTime? = null,
        val endTime: LocalDateTime? = null,
        val status: BookingStatus = BookingStatus.PENDING,
        val subject: String,
        val topics: Set<String> = mutableSetOf(),
        val invoice: Invoice? = null,
        val address: BookingAddress? = null,
        val studentId: Long? = null,
        val tutorId: Long? = null
) {

    fun toDto() = BookingDto(
            id = this.id,
            board = this.board.name,
            cancellationReason = this.cancellationReason,
            classNumber = this.classNumber,
            comment = this.comment,
            deadline = this.deadline,
            scheduledTime = this.scheduledTime,
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
                deadline = dto.scheduledTime.minusHours(DEADLINE_HOURS),
                invoice = Invoice.fromDto(dto.invoice)
        )

        fun fromDto(dto: UpdateBookingDto, default: Booking) = default.copy(
                topics = dto.topics?.toSet() ?: default.topics,
                scheduledTime = dto.scheduledTime ?: default.scheduledTime,
                deadline = dto.scheduledTime?.minusHours(DEADLINE_HOURS) ?: default.deadline,
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
