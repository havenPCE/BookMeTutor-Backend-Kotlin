package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Subject
import com.pce.kotlin.bookmetutor.util.SubjectName
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
@Suppress("UNCHECKED_CAST")
class SubjectRowMapper : RowMapper<Subject> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Subject {
        return Subject(
                id = rs.getLong("subject_id"),
                subjectName = SubjectName.valueOf(rs.getString("subject_name")),
                classNumber = rs.getInt("class_number"),
                topics = (rs.getArray("topics").array as Array<String>).toMutableSet()
        )
    }
}