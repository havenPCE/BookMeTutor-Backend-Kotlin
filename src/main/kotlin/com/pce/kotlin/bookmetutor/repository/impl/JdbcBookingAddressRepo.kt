package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.BookingAddress
import com.pce.kotlin.bookmetutor.repository.BookingAddressRepo
import com.pce.kotlin.bookmetutor.util.BookingAddressQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcBookingAddressRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : BookingAddressRepo {

    val bookingAddressRowMapper: (ResultSet, Int) -> BookingAddress = { rs, _ ->
        BookingAddress(
                id = rs.getLong("address_id"),
                line1 = rs.getString("line_1"),
                line2 = rs.getString("line_2"),
                landmark = rs.getString("landmark"),
                city = rs.getString("city"),
                pinCode = rs.getString("pin_code")
        )
    }

    override fun findById(id: Long): BookingAddress? {
        val (selectQuery, selectParams) = BookingAddressQuery.selectById(id)
        return jdbcTemplate.query(selectQuery, selectParams, bookingAddressRowMapper).firstOrNull()
    }

    override fun save(bookingId: Long, address: BookingAddress): BookingAddress? {
        val (insertQuery, insertParams) = BookingAddressQuery.insert(bookingId, address)
        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(address: BookingAddress): BookingAddress? {
        val (updateQuery, updateParams) = BookingAddressQuery.update(address)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = BookingAddressQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findByBookingId(bookingId: Long): BookingAddress? {
        val (selectQuery, selectParams) = BookingAddressQuery.selectByBookingId(bookingId)
        return jdbcTemplate.query(selectQuery, selectParams, bookingAddressRowMapper).firstOrNull()
    }
}