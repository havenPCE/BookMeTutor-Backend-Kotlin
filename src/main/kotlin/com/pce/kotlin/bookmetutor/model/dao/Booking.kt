package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.CreateInvoiceDto
import com.pce.kotlin.bookmetutor.model.dto.invoice.InvoiceDto
import com.pce.kotlin.bookmetutor.util.*
import net.bytebuddy.utility.RandomString
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "booking")
data class Booking(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "booking_id", unique = true, nullable = false)
        var id: Long? = null,

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
        var address: BookingAddress? = null,

        @Column(name = "scheduled_time", nullable = false)
        var scheduledTime: LocalDateTime,

        @Column(name = "deadline", nullable = false)
        var deadline: LocalDateTime,

        @Column(name = "start_time")
        var startTime: LocalDateTime? = null,

        @Column(name = "end_time")
        var endTime: LocalDateTime? = null,

        @Column(name = "rescheduled", nullable = false)
        var rescheduled: Boolean = false,

        @Column(name = "secret", nullable = false)
        var secret: String,

        @Column(name = "score", nullable = false)
        var score: Int = 0,

        @Column(name = "comment")
        var comment: String? = null,

        @Column(name = "cancellation_reason")
        var cancellationReason: String? = null,

        @Column(name = "rescheduling_reason")
        var reschedulingReason: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "booking_status", nullable = false)
        var status: BookingStatus = BookingStatus.PENDING,

        @OneToOne(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
        var invoice: Invoice? = null,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "student_id")
        var student: Student? = null,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor? = null

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

    companion object Util {
        fun fromDto(dto: CreateBookingDto): Booking = Booking(
                subject = SubjectName.valueOf(dto.subject),
                topics = dto.topics.toSet(),
                classNumber = dto.classNumber,
                board = Board.valueOf(dto.board),
                address = BookingAddress.fromDto(dto.address),
                scheduledTime = dto.scheduledTime,
                deadline = dto.scheduledTime.minusHours(Constants.DEADLINE_HOURS),
                secret = RandomString.make(Constants.SECRET_OTP_LENGTH)
        )

        fun fromDto(dto: UpdateBookingDto, booking: Booking): Booking = booking.copy(
                topics = dto.topics?.toSet() ?: booking.topics,
                scheduledTime = dto.scheduleTime ?: booking.scheduledTime,
                startTime = dto.startTime ?: booking.startTime,
                endTime = dto.endTime ?: booking.endTime,
                rescheduled = dto.rescheduled ?: booking.rescheduled,
                score = dto.score ?: booking.score,
                comment = dto.comment ?: booking.comment,
                cancellationReason = dto.cancellationReason ?: booking.cancellationReason,
                reschedulingReason = dto.reschedulingReason ?: booking.reschedulingReason,
                status = dto.status?.let { BookingStatus.valueOf(it) } ?: booking.status,
                rejects = dto.reject?.let { booking.rejects.plus(it) } ?: booking.rejects
        ).apply {
            if (rescheduled)
                deadline = scheduledTime.minusHours(Constants.DEADLINE_HOURS)
        }
    }
}

@Entity
@Table(name = "booking_address")
data class BookingAddress(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "address_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "line_1", nullable = false)
        var line1: String,

        @Column(name = "line_2")
        var line2: String? = null,

        @Column(name = "landmark")
        var landmark: String? = null,

        @Column(name = "city", nullable = false)
        var city: String,

        @Column(name = "pin_code", nullable = false, length = 6)
        var pinCode: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "booking_id")
        var booking: Booking? = null


) {
    fun toDto(): AddressDto? = AddressDto(
            id = this.id ?: -1,
            line1 = this.line1,
            line2 = this.line2,
            landmark = this.landmark,
            city = this.city,
            pinCode = this.pinCode
    )

    companion object Util {
        fun fromDto(dto: CreateAddressDto): BookingAddress = BookingAddress(
                line1 = dto.line1,
                line2 = dto.line2,
                landmark = dto.landmark,
                city = dto.city,
                pinCode = dto.pinCode
        )

        fun fromDto(dto: UpdateAddressDto, address: BookingAddress): BookingAddress = address.copy(
                line1 = dto.line1 ?: address.line1,
                line2 = dto.line2 ?: address.line2,
                landmark = dto.landmark ?: address.landmark,
                city = dto.city ?: address.city,
                pinCode = dto.pinCode ?: address.pinCode
        )
    }
}


@Entity
@Table(name = "booking_invoice")
class Invoice(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "invoice_id", unique = true, nullable = false)
        var id: Long? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "method", nullable = false)
        var method: PaymentMethod,

        @Column(name = "amount", nullable = false)
        var amount: Double,

        @Column(name = "summary")
        var summary: String? = null,

        @OneToOne(cascade = [CascadeType.ALL])
        var booking: Booking? = null

) {
    fun toDto(): InvoiceDto? = InvoiceDto(
            id = this.id ?: -1,
            method = this.method.name,
            amount = this.amount,
            summary = this.summary
    )

    companion object Util {
        fun fromDto(dto: CreateInvoiceDto): Invoice = Invoice(
                method = PaymentMethod.valueOf(dto.method),
                amount = dto.amount,
                summary = dto.summary
        )
    }
}