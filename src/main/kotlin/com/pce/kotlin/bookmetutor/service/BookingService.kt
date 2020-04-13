package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.model.dto.booking.CreateBookingDto
import com.pce.kotlin.bookmetutor.model.dto.booking.UpdateBookingDto
import com.pce.kotlin.bookmetutor.util.Gender

interface BookingService {

    fun createBooking(email: String, dto: CreateBookingDto): Booking?

    fun updateBooking(id: Long, dto: UpdateBookingDto): Booking?

    fun removeBooking(id: Long): Boolean

    fun retrieveBooking(id: Long): Booking?

    fun retrieveAllBookings(): List<Booking>

    fun assignBookingTutor(id: Long, gender: Gender, city: String): Booking?
}