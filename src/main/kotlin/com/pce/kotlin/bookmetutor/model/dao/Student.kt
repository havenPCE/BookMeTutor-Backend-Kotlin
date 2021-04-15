package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.util.Gender
import java.time.LocalDateTime
import kotlin.random.Random

data class Student(
    val id: Long = Random.nextLong(Long.MAX_VALUE),
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String? = null,
    val gender: Gender,
    val phones: Set<String> = mutableSetOf(),
    val registered: LocalDateTime = LocalDateTime.now(),
    val verified: Boolean = false,
    val addresses: Set<StudentAddress> = mutableSetOf(),
    val bookings: Set<Booking> = mutableSetOf()
) {
    fun toDto() = StudentDto(
        id = this.id,
        email = this.email,
        firstName = this.firstName,
        lastName = this.lastName,
        gender = this.gender.name,
        phones = this.phones.toList(),
        registered = this.registered,
        verified = this.verified,
        addresses = this.addresses.map { it.toDto() },
        bookings = this.bookings.map { it.toDto() }
    )

    companion object {
        fun fromDto(dto: CreateStudentDto) = Student(
            email = dto.email,
            password = dto.password,
            firstName = dto.firstName,
            lastName = dto.lastName,
            gender = Gender.valueOf(dto.gender),
            phones = mutableSetOf(dto.phone)
        )

        fun fromDto(dto: UpdateStudentDto, default: Student) = default.copy(
            password = dto.password ?: default.password,
            firstName = dto.firstName ?: default.firstName,
            lastName = dto.lastName ?: default.lastName,
            verified = dto.verified ?: default.verified
        )
    }
}