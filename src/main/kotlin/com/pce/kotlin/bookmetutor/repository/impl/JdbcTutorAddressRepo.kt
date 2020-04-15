package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.util.TutorAddressQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcTutorAddressRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : TutorAddressRepo {

    val tutorAddressRowMapper: (ResultSet, Int) -> TutorAddress = { rs, _ ->
        TutorAddress(
                id = rs.getLong("address_id"),
                line1 = rs.getString("line_1"),
                line2 = rs.getString("line_2"),
                landmark = rs.getString("landmark"),
                city = rs.getString("city"),
                pinCode = rs.getString("pin_code"),
                tutorId = rs.getLong("tutor_id")
        )
    }

    override fun findById(id: Long): TutorAddress? {
        val (selectQuery, selectParams) = TutorAddressQuery.selectById(id)
        return jdbcTemplate.query(selectQuery, selectParams, tutorAddressRowMapper).firstOrNull()
    }

    override fun save(tutorId: Long, address: TutorAddress): TutorAddress? {
        val (insertQuery, insertParams) = TutorAddressQuery.insert(tutorId, address)
        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(address: TutorAddress): TutorAddress? {
        val (updateQuery, updateParams) = TutorAddressQuery.update(address)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = TutorAddressQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findByTutorId(tutorId: Long): TutorAddress? {
        val (selectQuery, selectParams) = TutorAddressQuery.selectByTutorId(tutorId)
        return jdbcTemplate.query(selectQuery, selectParams, tutorAddressRowMapper).firstOrNull()
    }
}