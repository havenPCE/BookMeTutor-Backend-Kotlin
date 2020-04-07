package com.pce.kotlin.bookmetutor.repository

import com.pce.kotlin.bookmetutor.model.dao.BookingAddress

interface BookingAddressRepo {
    fun findById(id: Long): BookingAddress?
    fun save(address: BookingAddress): BookingAddress?
    fun update(address: BookingAddress): BookingAddress?
    fun deleteById(id: Long): Boolean
    fun findAll(): List<BookingAddress>?
}