package com.pce.kotlin.bookmetutor.repository.mapper

import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TopicRowMapper : RowMapper<String> {
    override fun mapRow(rs: ResultSet, rowNum: Int): String? {
        return rs.getString("topic")
    }
}