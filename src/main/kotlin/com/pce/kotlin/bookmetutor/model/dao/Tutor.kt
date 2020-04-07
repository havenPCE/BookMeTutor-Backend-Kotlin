package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import java.util.*
import kotlin.random.Random

data class Tutor(
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val email: String,
        val password: String,
        val gender: Gender,
        val lastPicked: Date = Date(),
        val firstName: String,
        val lastName: String? = null,
        val phones: Set<String> = mutableSetOf(),
        val registered: Date = Date(),
        val screening: Screening = Screening.PENDING,
        val verified: Boolean = false,
        val address: TutorAddress? = null,
        val qualification: TutorQualification? = null,
        val bookings: Set<Booking> = mutableSetOf()
) {
    fun toDto() = TutorDto(
            id = this.id,
            email = this.email,
            password = this.password,
            gender = this.gender.name,
            lastPicked = this.lastPicked,
            firstName = this.firstName,
            lastName = this.lastName,
            phones = this.phones.toList(),
            registered = this.registered,
            screening = this.screening.name,
            verified = this.verified,
            address = this.address?.toDto(),
            qualification = this.qualification?.toDto(),
            bookings = this.bookings.map { it.toDto() }
    )

    companion object {
        fun fromDto(dto: CreateTutorDto) = Tutor(
                email = dto.email,
                password = dto.password,
                gender = Gender.valueOf(dto.gender),
                firstName = dto.firstName,
                lastName = dto.lastName,
                phones = mutableSetOf(dto.phone),
                address = TutorAddress.fromDto(dto.address),
                qualification = TutorQualification.fromDto(dto.qualification)
        )

        fun fromDto(dto: UpdateTutorDto, default: Tutor) = default.copy(
                password = dto.password ?: default.password,
                firstName = dto.firstName ?: default.firstName,
                lastName = dto.lastName ?: default.lastName,
                verified = dto.verified ?: default.verified,
                lastPicked = dto.lastPicked ?: default.lastPicked
        )
    }
}