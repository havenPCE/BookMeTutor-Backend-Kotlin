package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.TutorQualification
import com.pce.kotlin.bookmetutor.repository.TutorQualificationRepo
import com.pce.kotlin.bookmetutor.repository.mapper.TutorQualificationRowMapper
import com.pce.kotlin.bookmetutor.util.TutorQualificationQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcTutorQualificationRepo(val jdbcTemplate: NamedParameterJdbcTemplate, val qualificationRowMapper: TutorQualificationRowMapper) : TutorQualificationRepo {
    override fun findById(id: Long): TutorQualification? {
        val (selectQuery, selectParams) = TutorQualificationQuery.selectById(id)
        return jdbcTemplate.query(selectQuery, selectParams, qualificationRowMapper).firstOrNull()
    }

    override fun save(tutorId: Long, qualification: TutorQualification): TutorQualification? {
        val (insertQuery, insertParams) = TutorQualificationQuery.insert(tutorId, qualification)
        return try {
            jdbcTemplate.update(insertQuery, insertParams)
            findById(qualification.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(qualification: TutorQualification): TutorQualification? {
        val (updateQuery, updateParams) = TutorQualificationQuery.update(qualification)
        return try {
            jdbcTemplate.update(updateQuery, updateParams)
            findById(qualification.id)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        val (deleteQuery, deleteParams) = TutorQualificationQuery.deleteById(id)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }
}