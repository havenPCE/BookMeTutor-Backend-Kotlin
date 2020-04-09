package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.TutorQualification
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TutorQualificationRowMapper : RowMapper<TutorQualification> {
    override fun mapRow(rs: ResultSet, rowNum: Int): TutorQualification {
        return TutorQualification(
                id = rs.getLong("qualification_id"),
                degree = rs.getString("degree"),
                university = rs.getString("university"),
                percentile = rs.getDouble("percentile")
        )
    }

}