package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.User
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.TutorAddressRepo
import com.pce.kotlin.bookmetutor.repository.TutorQualificationRepo
import com.pce.kotlin.bookmetutor.repository.TutorRepo
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import com.pce.kotlin.bookmetutor.util.TutorQuery
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
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

    val tutorIdRowMapper: (ResultSet, Int) -> Long = { rs, _ -> rs.getLong("tutor_id") }
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
                screening = Screening.valueOf(rs.getString("screening")),
                verified = rs.getBoolean("verified")
        )
    }
    val userRowMapper: (ResultSet, Int) -> User = { rs, _ ->
        User(
                userName = rs.getString("tutor_email"),
                password = rs.getString("tutor_password"),
                verified = rs.getBoolean("verified")
        )
    }

    override fun findById(id: Long): Tutor? {
        val (selectByEmailQuery, selectByEmailParams) = TutorQuery.selectById(id)
        var tutor: Tutor? = selectTutor(selectByEmailQuery, selectByEmailParams)
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

    override fun findByEmail(email: String): Tutor? {
        val (selectByEmailQuery, selectByEmailParams) = TutorQuery.selectByEmail(email)
        var tutor: Tutor? = selectTutor(selectByEmailQuery, selectByEmailParams)
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

    fun selectTutor(query: String, params: MapSqlParameterSource): Tutor? {
        return jdbcTemplate.query(query, params, tutorRowMapper).firstOrNull()
    }

    override fun findTutorForAssignment(city: String, rejects: List<String>): Tutor? {
        val (selectTutorQuery, selectTutorParams) = TutorQuery.selectByRequirement(city, rejects)
        return jdbcTemplate.query(selectTutorQuery, selectTutorParams, tutorIdRowMapper)
                .firstOrNull()?.let { findById(it) }
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
        val selectQuery = "SELECT tutor_id FROM public.tutor;"
        return jdbcTemplate.query(selectQuery, tutorIdRowMapper)
                .mapNotNull { findById(it) }
    }

    override fun findUser(email: String): User? {
        val (query, params) = TutorQuery.selectUser(email)
        return jdbcTemplate.query(query, params, userRowMapper).firstOrNull()
    }

    fun phoneBatchParams(phones: Collection<String>): Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(phones.map { mutableMapOf("phone" to it) }.toTypedArray())
}