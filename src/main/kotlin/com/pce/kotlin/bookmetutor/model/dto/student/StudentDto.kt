package com.pce.kotlin.bookmetutor.model.dto.student

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import java.util.*

data class StudentDto(
        val id: Long?,
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String?,
        val gender: String,
        val verified: Boolean,
        val registered: Date,
        val phones: List<String>,
        val addresses: List<AddressDto>,
        val bookings: List<BookingDto>
)