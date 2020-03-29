package com.pce.kotlin.bookmetutor.model.dto.tutor

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.QualificationDto
import java.time.LocalDateTime

data class TutorDto(
        val id: Long?,
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String?,
        val gender: String,
        val address: AddressDto,
        val qualification: QualificationDto,
        val verified: Boolean,
        val screening: String,
        val registered: LocalDateTime,
        val lastPicked: LocalDateTime,
        val phones: List<String>,
        val bookings: List<BookingDto>
)