package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dto.booking.BookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto

interface BookingService {

    fun createBooking(email: String, dto: CreateBookingDto): BookingDto?

    fun updateBooking(id: Long, dto: UpdateBookingDto): BookingDto?

    fun removeBooking(id: Long): Boolean

    fun retrieveBooking(id: Long): BookingDto?

    fun retrieveAllBookings(): List<BookingDto>

    fun reassignTutor(id: Long): BookingDto?
}