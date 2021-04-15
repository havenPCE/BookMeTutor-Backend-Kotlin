package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.model.dao.User
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.util.AdminQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcAdminRepo(val jdbcTemplate: NamedParameterJdbcTemplate) : AdminRepo {

    val adminRowMapper: (ResultSet, Int) -> Admin = { rs, _ ->
        Admin(
            id = rs.getLong("admin_id"),
            email = rs.getString("admin_email"),
            password = rs.getString("admin_password"),
            verified = rs.getBoolean("admin_verified")
        )
    }

    val userRowMapper: (ResultSet, Int) -> User = { rs, _ ->
        User(
            userName = rs.getString("admin_email"),
            password = rs.getString("admin_password"),
            verified = rs.getBoolean("admin_verified")
        )
    }

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

    override fun findById(id: Long): Admin? {
        val (selectQuery, selectParams) = AdminQuery.selectById(id)
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
        val selectQuery = "SELECT admin_id, admin_email, admin_password, admin_verified FROM public.admin;"
        return jdbcTemplate.query(selectQuery, adminRowMapper)
    }

    override fun findUser(email: String): User? {
        val (query, params) = AdminQuery.selectUser(email)
        return jdbcTemplate.query(query, params, userRowMapper).firstOrNull()
    }
}