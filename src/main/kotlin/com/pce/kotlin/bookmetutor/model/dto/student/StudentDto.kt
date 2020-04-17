package com.pce.kotlin.bookmetutor.model.dto.student

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import java.time.LocalDateTime

data class StudentDto(
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String? = null,
        val gender: String,
        val verified: Boolean,
        val registered: LocalDateTime,
        val phones: List<String>,
        val addresses: List<AddressDto>,
        val bookings: List<BookingDto>
)