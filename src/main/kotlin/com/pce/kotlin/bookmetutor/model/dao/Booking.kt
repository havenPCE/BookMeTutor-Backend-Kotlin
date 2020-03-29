package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import com.pce.kotlin.bookmetutor.util.Board
import com.pce.kotlin.bookmetutor.util.BookingStatus
import com.pce.kotlin.bookmetutor.util.PaymentMethod
import com.pce.kotlin.bookmetutor.util.SubjectName
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "booking")
data class Booking(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "booking_id", unique = true, nullable = false)
        var id: Long?,

        @Enumerated(EnumType.STRING)
        @Column(name = "subject", nullable = false)
        var subject: SubjectName,

        @ElementCollection
        var topics: Set<String> = emptySet(),

        @ElementCollection
        var rejects: Set<String> = emptySet(),

        @Column(name = "class", nullable = false)
        var classNumber: Int,

        @Enumerated(EnumType.STRING)
        @Column(name = "board", nullable = false)
        var board: Board,

        @OneToOne(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
        var address: BookingAddress?,

        @Column(name = "scheduled_time", nullable = false)
        var scheduledTime: LocalDateTime,

        @Column(name = "deadline", nullable = false)
        var deadline: LocalDateTime,

        @Column(name = "start_time")
        var startTime: LocalDateTime?,

        @Column(name = "end_time")
        var endTime: LocalDateTime?,

        @Column(name = "rescheduled", nullable = false)
        var rescheduled: Boolean = false,

        @Column(name = "secret", nullable = false)
        var secret: String,

        @Column(name = "score", nullable = false)
        var score: Int = 0,

        @Column(name = "comment")
        var comment: String?,

        @Column(name = "cancellation_reason")
        var cancellationReason: String?,

        @Column(name = "rescheduling_reason")
        var reschedulingReason: String?,

        @Enumerated(EnumType.STRING)
        @Column(name = "booking_status", nullable = false)
        var status: BookingStatus = BookingStatus.PENDING,

        @OneToOne(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
        var invoice: Invoice?,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "student_id")
        var student: Student?,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor?

) {
    fun toDto(): BookingDto? = BookingDto(
            id = this.id ?: -1,
            subject = this.subject.name,
            topics = this.topics.map { it },
            rejects = this.rejects.map { it },
            classNumber = this.classNumber,
            board = this.board.name,
            address = this.address?.toDto(),
            scheduleTime = this.scheduledTime,
            deadline = this.deadline,
            startTime = this.startTime,
            endTime = this.endTime,
            rescheduled = this.rescheduled,
            secret = this.secret,
            score = this.score,
            comment = this.comment,
            cancellationReason = this.cancellationReason,
            reschedulingReason = this.reschedulingReason,
            status = this.status.name,
            invoice = this.invoice?.toDto(),
            studentName = "${this.student?.firstName} ${this.student?.lastName}",
            studentNumber = this.student?.phones?.map { it },
            studentEmail = this.student?.email,
            tutorName = "${this.tutor?.firstName} ${this.tutor?.lastName}",
            tutorNumber = this.tutor?.phones?.map { it },
            tutorEmail = this.tutor?.email
    )
}

@Entity
@Table(name = "booking_address")
data class BookingAddress(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "address_id", unique = true, nullable = false)
        var id: Long?,

        @Column(name = "line_1", nullable = false)
        var line1: String,

        @Column(name = "line_2")
        var line2: String?,

        @Column(name = "landmark")
        var landmark: String?,

        @Column(name = "city", nullable = false)
        var city: String,

        @Column(name = "pin_code", nullable = false, length = 6)
        var pinCode: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "booking_id")
        var booking: Booking?


) {
    fun toDto(): AddressDto? = AddressDto(
            id = this.id ?: -1,
            line1 = this.line1,
            line2 = this.line2,
            landmark = this.landmark,
            city = this.city,
            pinCode = this.pinCode
    )
}


@Entity
@Table(name = "booking_invoice")
class Invoice(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "invoice_id", unique = true, nullable = false)
        var id: Long?,

        @Enumerated(EnumType.STRING)
        @Column(name = "method", nullable = false)
        var method: PaymentMethod,

        @Column(name = "amount", nullable = false)
        var amount: Double,

        @Column(name = "summary")
        var summary: String?,

        @OneToOne(cascade = [CascadeType.ALL])
        var booking: Booking?

) {
    fun toDto(): InvoiceDto? = InvoiceDto(
            id = this.id ?: -1,
            method = this.method.name,
            amount = this.amount,
            summary = this.summary
    )
}