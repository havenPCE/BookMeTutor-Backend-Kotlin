package com.pce.kotlin.bookmetutor.model.dto.tutor

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.QualificationDto
import java.time.LocalDateTime

data class TutorDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String? = null,
    val gender: String,
    val address: AddressDto? = null,
    val qualification: QualificationDto? = null,
    val verified: Boolean,
    val screening: String,
    val registered: LocalDateTime,
    val lastPicked: LocalDateTime,
    val phones: List<String>,
    val bookings: List<BookingDto>
)