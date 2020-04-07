package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Admin
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class AdminRowMapper : RowMapper<Admin> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Admin {
        return Admin(
                id = rs.getLong("admin_id"),
                email = rs.getString("admin_email"),
                password = rs.getString("admin_password")
        )
    }
}