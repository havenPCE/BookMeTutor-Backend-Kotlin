package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.repository.mapper.AdminRowMapper
import com.pce.kotlin.bookmetutor.util.AdminQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcAdminRepo(val jdbcTemplate: NamedParameterJdbcTemplate, val adminRowMapper: AdminRowMapper) : AdminRepo {

    override fun save(admin: Admin): Admin? {
        val (insertQuery, insertParams) = AdminQuery.insert(admin)

        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findByEmail(admin.email)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(admin: Admin): Admin? {
        val (updateQuery, updateParams) = AdminQuery.update(admin)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findByEmail(admin.email)
        } catch (e: Exception) {
            null
        }
    }

    override fun findByEmail(email: String): Admin? {
        val (selectQuery, selectParams) = AdminQuery.selectByEmail(email)
        return jdbcTemplate.query(selectQuery, selectParams, adminRowMapper).firstOrNull()
    }

    override fun deleteByEmail(email: String): Boolean {
        val (deleteQuery, deleteParams) = AdminQuery.deleteByEmail(email)
        return try {
            jdbcTemplate.update(deleteQuery, deleteParams) > 0
        } catch (e: Exception) {
            false
        }
    }

    override fun findAll(): List<Admin> {
        val selectQuery = "SELECT admin_id, admin_email, admin_password FROM public.admin;"
        return jdbcTemplate.query(selectQuery, adminRowMapper)
    }
}