package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.model.dao.User
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.StudentAddressRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.StudentQuery
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.SqlParameterSource
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet

@Repository
@Transactional(rollbackFor = [Throwable::class])
class JdbcStudentRepo(val jdbcTemplate: NamedParameterJdbcTemplate,
                      val studentAddressRepo: StudentAddressRepo,
                      val bookingRepo: BookingRepo) : StudentRepo {

    val studentIdRowMapper: (ResultSet, Int) -> Long = { rs, _ -> rs.getLong("student_id") }
    val phoneRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("phone") }
    val studentRowMapper: (ResultSet, Int) -> Student = { rs, _ ->
        Student(
                id = rs.getLong("student_id"),
                email = rs.getString("email"),
                password = rs.getString("password"),
                firstName = rs.getString("first_name"),
                lastName = rs.getString("last_name"),
                gender = Gender.valueOf(rs.getString("gender")),
                phones = emptySet(),
                registered = rs.getTimestamp("registered").toLocalDateTime(),
                verified = rs.getBoolean("verified")
        )
    }
    val userRowMapper: (ResultSet, Int) -> User = { rs, _ ->
        User(
                userName = rs.getString("email"),
                password = rs.getString("password"),
                verified = rs.getBoolean("verified")
        )
    }

    override fun findById(id: Long): Student? {
        val (selectStudentQuery, selectStudentParams) = StudentQuery.selectByIdFromStudent(id)
        var student: Student? = selectStudent(selectStudentQuery, selectStudentParams)
        student = student?.let {
            val (selectPhoneQuery, selectPhoneParams) = StudentQuery.selectPhone(it.id)
            it.copy(
                    phones = jdbcTemplate.query(selectPhoneQuery, selectPhoneParams, phoneRowMapper).toSet(),
                    addresses = studentAddressRepo.findByStudentId(it.id).toSet(),
                    bookings = bookingRepo.findByStudentId(it.id).toSet()
            )
        }
        return student
    }

    fun selectStudent(query: String, params: MapSqlParameterSource): Student? {
        return jdbcTemplate.query(query, params, studentRowMapper).firstOrNull()
    }

    override fun findByEmail(email: String): Student? {
        val (selectStudentQuery, selectStudentParams) = StudentQuery.selectByEmailFromStudent(email)
        var student: Student? = selectStudent(selectStudentQuery, selectStudentParams)
        student = student?.let {
            val (selectPhoneQuery, selectPhoneParams) = StudentQuery.selectPhone(it.id)
            it.copy(
                    phones = jdbcTemplate.query(selectPhoneQuery, selectPhoneParams, phoneRowMapper).toSet(),
                    addresses = studentAddressRepo.findByStudentId(it.id).toSet(),
                    bookings = bookingRepo.findByStudentId(it.id).toSet()
            )
        }
        return student
    }

    override fun save(student: Student): Student? {
        val (insertStudentQuery, insertStudentParams) = StudentQuery.insertIntoStudent(student)
        val insertPhoneQuery = StudentQuery.insertIntoPhone(student.id)
        val insertPhoneParams = phoneBatchParams(student.phones)
        return try {
            jdbcTemplate.update(insertStudentQuery, insertStudentParams)
            jdbcTemplate.batchUpdate(insertPhoneQuery, insertPhoneParams)
            findByEmail(student.email)
        } catch (e: Exception) {
            null
        }

    }

    override fun update(student: Student): Student? {
        val (updateStudentQuery, updateStudentParams) = StudentQuery.updateStudent(student)
        val (deletePhoneQuery, deletePhoneParams) = StudentQuery.deletePhone(student.id)
        val insertPhoneQuery = StudentQuery.insertIntoPhone(student.id)
        val insertPhoneParams = phoneBatchParams(student.phones)
        return try {
            jdbcTemplate.update(updateStudentQuery, updateStudentParams)
            jdbcTemplate.update(deletePhoneQuery, deletePhoneParams)
            jdbcTemplate.batchUpdate(insertPhoneQuery, insertPhoneParams)
            findByEmail(student.email)
        } catch (e: Exception) {
            null
        }
    }

    override fun deleteByEmail(email: String): Boolean {
        val (deleteQuery, deleteParams) = StudentQuery.deleteByEmail(email)
        return jdbcTemplate.update(deleteQuery, deleteParams) > 0
    }

    override fun findAll(): List<Student> {
        val selectQuery = """SELECT student_id FROM public.student;"""
        return jdbcTemplate.query(selectQuery, studentIdRowMapper)
                .mapNotNull { findById(it) }
    }

    override fun findUser(email: String): User? {
        val (query, params) = StudentQuery.selectUser(email)
        return jdbcTemplate.query(query, params, userRowMapper).firstOrNull()
    }

    fun phoneBatchParams(phones: Collection<String>): Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(phones.map { mutableMapOf("phone" to it) }.toTypedArray())
}