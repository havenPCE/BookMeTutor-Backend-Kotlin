package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Booking

interface BookingRepo {
    fun findById(id: Long): Booking?
    fun save(studentId: Long, tutorId: Long, booking: Booking): Booking?
    fun update(tutorId: Long, booking: Booking): Booking?
    fun deleteById(id: Long): Boolean
    fun findAll(): List<Booking>
    fun findByStudentId(studentId: Long): List<Booking>
    fun findByTutorId(tutorId: Long): List<Booking>
}