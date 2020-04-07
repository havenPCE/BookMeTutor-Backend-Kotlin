package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.repository.mapper.AdminRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcAdminRepo(val jdbcTemplate: JdbcTemplate, val adminRowMapper: AdminRowMapper) : AdminRepo {

    override fun save(admin: Admin): Admin? {
        val insertSql = "INSERT INTO public.admin(admin_id, admin_email, admin_password) VALUES (?, ?, ?);"

        val selectSql = "SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_id = ?;"

        return try {
            jdbcTemplate.update(insertSql, admin.id, admin.email, admin.password)

            jdbcTemplate.query(selectSql, adminRowMapper, admin.id).firstOrNull()
        } catch (e: Exception) {
            null
        }

    }

    override fun update(admin: Admin): Admin? {
        val updateSql = """UPDATE public.admin SET admin_id=?, admin_email=?, admin_password=? WHERE admin_id = ?;"""

        val selectSql = """SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_id = ?;"""

        return try {
            jdbcTemplate.update(updateSql, admin.id, admin.email, admin.password, admin.id)

            return jdbcTemplate.query(selectSql, adminRowMapper, admin.id).firstOrNull()
        } catch (e: Exception) {
            null
        }

    }

    override fun findByEmail(email: String): Admin? {
        val selectSql = """SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_email = ?;"""
        return jdbcTemplate.query(selectSql, adminRowMapper, email).firstOrNull()
    }

    override fun deleteByEmail(email: String): Boolean {
        val deleteSql = """DELETE FROM public.admin WHERE admin_email = ?;"""
        return jdbcTemplate.update(deleteSql, email) != 0
    }

    override fun findAll(): List<Admin>? {
        return try {
            val selectSql = "SELECT admin_id, admin_email, admin_password FROM public.admin;"
            return jdbcTemplate.query(selectSql, adminRowMapper)
        } catch (e: Exception) {
            null
        }

    }
}