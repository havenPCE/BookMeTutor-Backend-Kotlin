package com.pce.kotlin.bookmetutor.service.impl

import com.pce.kotlin.bookmetutor.service.AccountService
import com.pce.kotlin.bookmetutor.util.AccountQuery
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class JdbcAccountService(val jdbcTemplate: JdbcTemplate) : AccountService {
    override fun isPresent(email: String): Boolean {
        val (studentQuery, studentParams) = AccountQuery.student(email)
        val (tutorQuery, tutorParams) = AccountQuery.tutor(email)
        val (adminQuery, adminParams) = AccountQuery.admin(email)

        val result: Int = maxOf(
                jdbcTemplate.queryForObject(studentQuery, studentParams, Int::class.java),
                jdbcTemplate.queryForObject(tutorQuery, tutorParams, Int::class.java),
                jdbcTemplate.queryForObject(adminQuery, adminParams, Int::class.java)
        )
        return result > 0
    }
}