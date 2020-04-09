package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.StudentAddress
import com.pce.kotlin.bookmetutor.repository.StudentAddressRepo
import com.pce.kotlin.bookmetutor.util.StudentAddressQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcStudentAddressRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : StudentAddressRepo {

    val studentAddressRowMapper: (ResultSet, Int) -> StudentAddress = { rs, _ ->
        StudentAddress(
                id = rs.getLong("address_id"),
                line1 = rs.getString("line_1"),
                line2 = rs.getString("line_2"),
                landmark = rs.getString("landmark"),
                city = rs.getString("city"),
                pinCode = rs.getString("pin_code")
        )
    }

    override fun findById(id: Long): StudentAddress? {
        val (selectQuery, selectParams) = StudentAddressQuery.selectById(id)
        return jdbcTemplate.query(selectQuery, selectParams, studentAddressRowMapper).firstOrNull()
    }

    override fun save(studentId: Long, address: StudentAddress): StudentAddress? {
        val (insertQuery, insertParams) = StudentAddressQuery.insert(studentId, address)
        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(address: StudentAddress): StudentAddress? {
        val (updateQuery, updateParams) = StudentAddressQuery.update(address)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findById(address.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = StudentAddressQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findByStudentId(studentId: Long): List<StudentAddress> {
        val (selectQuery, selectParams) = StudentAddressQuery.selectByStudentId(studentId)
        return jdbcTemplate.query(selectQuery, selectParams, studentAddressRowMapper)
    }
}