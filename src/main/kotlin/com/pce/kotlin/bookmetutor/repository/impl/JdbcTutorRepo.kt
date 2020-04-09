package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcTutorRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : TutorRepo {
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