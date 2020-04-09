package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Booking
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcBookingRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : BookingRepo {
    override fun findById(id: Long): Booking? {
        TODO("Not yet implemented")
    }

    override fun save(booking: Booking): Booking? {
        TODO("Not yet implemented")
    }

    override fun update(booking: Booking): Booking? {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Booking> {
        TODO("Not yet implemented")
    }

}