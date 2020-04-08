package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.Booking

interface BookingRepo {
    fun findById(id: Long): Booking?
    fun save(booking: Booking): Booking?
    fun update(booking: Booking): Booking?
    fun deleteById(id: Long): Boolean
    fun findAll(): List<Booking>
}