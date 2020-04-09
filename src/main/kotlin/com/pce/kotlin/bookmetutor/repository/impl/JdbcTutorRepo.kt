package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcTutorRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : TutorRepo {

    val tutorRowMapper: (ResultSet, Int) -> Tutor = { rs, _ ->
        Tutor(
                id = rs.getLong("tutor_id"),
                email = rs.getString("tutor_email"),
                password = rs.getString("tutor_password"),
                gender = Gender.valueOf(rs.getString("gender")),
                lastPicked = rs.getTimestamp("last_picked").toLocalDateTime(),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                phones = emptySet(),
                registered = rs.getTimestamp("registered").toLocalDateTime(),
                screening = Screening.valueOf(rs.getString("screening"))
        )
    }

    override fun findByEmail(email: String): Tutor? {
        TODO("Not yet implemented")
    }

    override fun findTutorForAssignment(city: String, rejects: List<String>): Tutor? {
        TODO("Not yet implemented")
    }

    override fun save(tutor: Tutor): Tutor? {
        TODO("Not yet implemented")
    }

    override fun update(tutor: Tutor): Tutor? {
        TODO("Not yet implemented")
    }

    override fun deleteByEmail(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Tutor> {
        TODO("Not yet implemented")
    }
}