package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Admin
import com.pce.kotlin.bookmetutor.repository.AdminRepo
import com.pce.kotlin.bookmetutor.repository.mapper.AdminRowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = [Throwable::class])
@Repository
class JdbcAdminRepo(val jdbcTemplate: NamedParameterJdbcTemplate, val adminRowMapper: AdminRowMapper) : AdminRepo {

    override fun save(admin: Admin): Admin? {
        val insertSql = "INSERT INTO public.admin(admin_id, admin_email, admin_password) VALUES (:id, :email, :password);"
        val insertParameter = MapSqlParameterSource(mutableMapOf("id" to admin.id, "email" to admin.email, "password" to admin.password))
        val selectSql = "SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_id = :id;"
        val selectParameter = MapSqlParameterSource("id", admin.id)

        return try {
            jdbcTemplate.update(insertSql, insertParameter)
            jdbcTemplate.query(selectSql, selectParameter, adminRowMapper).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override fun update(admin: Admin): Admin? {
        val updateSql = """UPDATE public.admin SET admin_id=:id, admin_email=:email, admin_password=:password WHERE admin_id = :id;"""
        val updateParameter = MapSqlParameterSource(mutableMapOf("id" to admin.id, "email" to admin.email, "password" to admin.password))
        val selectSql = """SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_id = :id;"""
        val selectParameter = MapSqlParameterSource("id", admin.id)

        return try {
            jdbcTemplate.update(updateSql, updateParameter)
            return jdbcTemplate.query(selectSql, selectParameter, adminRowMapper).firstOrNull()
        } catch (e: Exception) {
            null
        }

    }

    override fun findByEmail(email: String): Admin? {
        val selectSql = """SELECT admin_id, admin_email, admin_password FROM public.admin WHERE admin_email = :email;"""
        val selectParameter = MapSqlParameterSource("email", email)
        return jdbcTemplate.query(selectSql, selectParameter, adminRowMapper).firstOrNull()
    }

    override fun deleteByEmail(email: String): Boolean {
        val deleteSql = """DELETE FROM public.admin WHERE admin_email = :email;"""
        val deleteParameter = MapSqlParameterSource("email", email)
        return try {
            jdbcTemplate.update(deleteSql, deleteParameter) != 0
        } catch (e: Exception) {
            false
        }
    }

    override fun findAll(): List<Admin> {
        val selectSql = "SELECT admin_id, admin_email, admin_password FROM public.admin;"
        return jdbcTemplate.query(selectSql, adminRowMapper)
    }
}