package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.repository.TutorQualificationRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import com.pce.kotlin.bookmetutor.util.TutorQuery
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcTutorRepo(val jdbcTemplate: NamedParameterJdbcTemplate,
                    val tutorAddressRepo: TutorAddressRepo,
                    val tutorQualificationRepo: TutorQualificationRepo,
                    val bookingRepo: BookingRepo) : TutorRepo {

    val emailRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("tutor_email") }
    val phoneRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("phone") }
    val tutorRowMapper: (ResultSet, Int) -> Tutor = { rs, _ ->
        Tutor(
                id = rs.getLong("tutor_id"),
                email = rs.getString("tutor_email"),
                password = rs.getString("tutor_password"),
                gender = Gender.valueOf(rs.getString("gender")),
                lastPicked = rs.getTimestamp("last_picked").toLocalDateTime(),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                phones = emptySet(),
                registered = rs.getTimestamp("registered").toLocalDateTime(),
                screening = Screening.valueOf(rs.getString("screening"))
        )
    }

    override fun findByEmail(email: String): Tutor? {
        val (selectByEmailQuery, selectByEmailParams) = TutorQuery.selectByEmail(email)
        var tutor: Tutor? = jdbcTemplate.query(selectByEmailQuery, selectByEmailParams, tutorRowMapper).firstOrNull()
        tutor = tutor?.let {
            val (selectPhoneQuery, selectPhoneParams) = TutorQuery.selectPhone(it.id)
            it.copy(
                    phones = jdbcTemplate.query(selectPhoneQuery, selectPhoneParams, phoneRowMapper).toSet(),
                    address = tutorAddressRepo.findByTutorId(it.id),
                    qualification = tutorQualificationRepo.findByTutorId(it.id),
                    bookings = bookingRepo.findByTutorId(it.id).toSet()
            )
        }
        return tutor
    }

    override fun findTutorForAssignment(gender: Gender, city: String, rejects: List<String>): Tutor? {
        val (selectTutorQuery, selectTutorParams) = TutorQuery.selectByRequirement(gender, city, rejects)
        return jdbcTemplate.query(selectTutorQuery, selectTutorParams, emailRowMapper).firstOrNull()?.let {
            findByEmail(it)
        }
    }

    override fun save(tutor: Tutor): Tutor? {
        val (insertTutorQuery, insertTutorParams) = TutorQuery.insertIntoTutor(tutor)
        val insertPhoneQuery = TutorQuery.insertPhone(tutor.id)
        val insertPhoneParams = phoneBatchParams(tutor.phones)
        return try {
            jdbcTemplate.update(insertTutorQuery, insertTutorParams)
            jdbcTemplate.batchUpdate(insertPhoneQuery, insertPhoneParams)
            tutor.address?.let {
                tutorAddressRepo.save(tutor.id, it)
            }
            tutor.qualification?.let {
                tutorQualificationRepo.save(tutor.id, it)
            }
            findByEmail(tutor.email)
        } catch (e: Exception) {
            null
        }
    }

    override fun update(tutor: Tutor): Tutor? {
        val (updateTutorQuery, updateTutorParams) = TutorQuery.updateTutor(tutor)
        val (deletePhoneQuery, deletePhoneParams) = TutorQuery.deletePhone(tutor.id)
        val insertPhoneQuery = TutorQuery.insertPhone(tutor.id)
        val insertPhoneParams = phoneBatchParams(tutor.phones)
        return try {
            jdbcTemplate.update(updateTutorQuery, updateTutorParams)
            jdbcTemplate.update(deletePhoneQuery, deletePhoneParams)
            jdbcTemplate.batchUpdate(insertPhoneQuery, insertPhoneParams)
            findByEmail(tutor.email)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteByEmail(email: String): Boolean {
        val (deleteQuery, deleteParams) = TutorQuery.deleteByEmail(email)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findAll(): List<Tutor> {
        val selectQuery = "SELECT tutor_email FROM public.tutor;"
        return jdbcTemplate.query(selectQuery, emailRowMapper)
                .mapNotNull { findByEmail(it) }
    }

    fun phoneBatchParams(phones: Collection<String>): Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(phones.map { mutableMapOf("phone" to it) }.toTypedArray())
}