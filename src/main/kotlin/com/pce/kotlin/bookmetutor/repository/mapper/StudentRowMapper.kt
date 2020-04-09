package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.util.Gender
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
@Suppress("UNCHECKED_CAST")
class StudentRowMapper : RowMapper<Student> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Student? {
        return Student(
                id = rs.getLong("student_id"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                gender = Gender.valueOf(rs.getString("gender")),
                phones = emptySet(),
                registered = rs.getTimestamp("registered"),
                verified = rs.getBoolean("verified")
        )
    }
}