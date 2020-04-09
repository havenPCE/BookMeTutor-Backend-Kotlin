package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.model.dao.TutorQualification
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
@Suppress("UNCHECKED_CAST")
class TutorRowMapper : RowMapper<Tutor> {
    override fun mapRow(rs: ResultSet, rowNum: Int): Tutor {
        return Tutor(
                id = rs.getLong("tutor_id"),
                email = rs.getString("tutor_email"),
                password = rs.getString("tutor_password"),
                gender = Gender.valueOf(rs.getString("gender")),
                lastPicked = rs.getTimestamp("last_picked"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                phones = emptySet(),
                registered = rs.getTimestamp("registered"),
                screening = Screening.valueOf(rs.getString("screening")),
                address = TutorAddress(
                        id = rs.getLong("address_id"),
                        line1 = rs.getString("line_1"),
                        line2 = rs.getString("line_2"),
                        landmark = rs.getString("landmark"),
                        city = rs.getString("city"),
                        pinCode = rs.getString("pin_code")
                ),
                qualification = TutorQualification(
                        id = rs.getLong("qualification_id"),
                        degree = rs.getString("degree"),
                        university = rs.getString("university"),
                        percentile = rs.getDouble("percentile")
                )
        )
    }
}