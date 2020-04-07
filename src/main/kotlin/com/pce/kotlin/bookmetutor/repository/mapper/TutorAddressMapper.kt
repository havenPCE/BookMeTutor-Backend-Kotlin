package com.pce.kotlin.bookmetutor.repository.mapper

import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet

@Component
class TutorAddressMapper : RowMapper<TutorAddress> {
    override fun mapRow(rs: ResultSet, rowNum: Int): TutorAddress {
        return TutorAddress(
                id = rs.getLong("address_id"),
                line1 = rs.getString("line_1"),
                line2 = rs.getString("line_2"),
                landmark = rs.getString("landmark"),
                city = rs.getString("city"),
                pinCode = rs.getString("pin_code")
        )
    }
}