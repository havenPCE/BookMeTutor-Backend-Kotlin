package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.util.Gender
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcStudentRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : StudentRepo {

    val emailRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("email") }
    val phoneRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("phone") }
    val studentRowMapper: (ResultSet, Int) -> Student = { rs, _ ->
        Student(
                id = rs.getLong("student_id"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                gender = Gender.valueOf(rs.getString("gender")),
                phones = emptySet(),
                registered = rs.getTimestamp("registered").toLocalDateTime(),
                verified = rs.getBoolean("verified")
        )
    }

    override fun findByEmail(email: String): Student? {
        TODO("Not yet implemented")
    }

    override fun save(student: Student): Student? {
        TODO("Not yet implemented")
    }

    override fun update(student: Student): Student? {
        TODO("Not yet implemented")
    }

    override fun deleteByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Student> {
        TODO("Not yet implemented")
    }
}