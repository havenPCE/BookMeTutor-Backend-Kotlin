package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcStudentRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : StudentRepo {
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