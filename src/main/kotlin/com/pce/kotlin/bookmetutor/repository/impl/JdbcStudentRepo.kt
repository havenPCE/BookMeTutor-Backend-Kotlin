package com.pce.kotlin.bookmetutor.repository.impl

import com.pce.kotlin.bookmetutor.model.dao.Student
import com.pce.kotlin.bookmetutor.repository.BookingRepo
import com.pce.kotlin.bookmetutor.repository.StudentAddressRepo
import com.pce.kotlin.bookmetutor.repository.StudentRepo
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.StudentQuery
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

    val emailRowMapper: (ResultSet, Int) -> String = { rs, _ -> rs.getString("email") }
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

    override fun findByEmail(email: String): Student? {
        val (selectStudentQuery, selectStudentParams) = StudentQuery.selectByEmailFromStudent(email)
        var student: Student? = jdbcTemplate.query(selectStudentQuery, selectStudentParams, studentRowMapper).firstOrNull()
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
        val selectQuery = """SELECT email FROM public.student;"""
        return jdbcTemplate.query(selectQuery, emailRowMapper)
                .mapNotNull { findByEmail(it) }
    }

    fun phoneBatchParams(phones: Collection<String>): Array<SqlParameterSource> = SqlParameterSourceUtils.createBatch(phones.map { mutableMapOf("phone" to it) }.toTypedArray())
}