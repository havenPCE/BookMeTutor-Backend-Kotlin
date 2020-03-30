package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.CreateQualificationDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.QualificationDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto
import com.pce.kotlin.bookmetutor.util.Gender
import com.pce.kotlin.bookmetutor.util.Screening
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "tutor")
data class Tutor(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "tutor_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "email", nullable = false, unique = true)
        var email: String,

        @Column(name = "password", nullable = false)
        var password: String,

        @Column(name = "first_name", nullable = false)
        var firstName: String,

        @Column(name = "last_name")
        var lastName: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "gender", nullable = false)
        var gender: Gender,

        @Column(name = "verified", nullable = false)
        var verified: Boolean = false,

        @Enumerated(EnumType.STRING)
        @Column(name = "screening", nullable = false)
        var screening: Screening = Screening.ACCEPTED,

        @Column(name = "registered")
        var registered: LocalDateTime,

        @Column(name = "last_picked", nullable = false)
        var lastPicked: LocalDateTime,

        @ElementCollection
        var phones: Set<String> = emptySet(),

        @OneToOne(mappedBy = "tutor", cascade = [CascadeType.ALL], orphanRemoval = true)
        var address: TutorAddress? = null,

        @OneToOne(mappedBy = "tutor", cascade = [CascadeType.ALL], orphanRemoval = true)
        var qualification: TutorQualification? = null,

        @OneToMany(mappedBy = "tutor", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        var bookings: Set<Booking> = emptySet()

) {
        fun toDto(): TutorDto? = TutorDto(
                id = this.id,
                email = this.email,
                password = this.password,
                firstName = this.firstName,
                lastName = this.lastName,
                gender = this.gender.name,
                verified = this.verified,
                screening = this.screening.name,
                registered = this.registered,
                lastPicked = this.lastPicked,
                phones = this.phones.map { it },
                address = this.address?.toDto(),
                qualification = this.qualification?.toDto(),
                bookings = this.bookings.mapNotNull { it.toDto() }
        )

        companion object Util {
                fun fromDto(dto: CreateTutorDto): Tutor = Tutor(
                        email = dto.email,
                        password = dto.password,
                        firstName = dto.firstName,
                        lastName = dto.lastName,
                        gender = Gender.valueOf(dto.gender),
                        registered = LocalDateTime.now(),
                        lastPicked = LocalDateTime.now(),
                        phones = setOf(dto.phone),
                        address = TutorAddress.fromDto(dto.address),
                        qualification = TutorQualification.fromDto(dto.qualification)
                )

                fun fromDto(dto: UpdateTutorDto, tutor: Tutor): Tutor = tutor.copy(
                        password = dto.password ?: tutor.password,
                        firstName = dto.firstName ?: tutor.firstName,
                        lastName = dto.lastName ?: tutor.lastName
                )
        }
}

@Entity
@Table(name = "tutor_address")
data class TutorAddress(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "address_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "line_1", nullable = false)
        var line1: String,

        @Column(name = "line_2")
        var line2: String? = null,

        @Column(name = "landmark")
        var landmark: String? = null,

        @Column(name = "city", nullable = false)
        var city: String,

        @Column(name = "pin_code", nullable = false, length = 6)
        var pinCode: String,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor? = null
) {
        fun toDto(): AddressDto? = AddressDto(
                id = this.id ?: -1,
                line1 = this.line1,
                line2 = this.line2,
                landmark = this.landmark,
                city = this.city,
                pinCode = this.pinCode
        )

        companion object Util {
                fun fromDto(dto: CreateAddressDto): TutorAddress = TutorAddress(
                        line1 = dto.line1,
                        line2 = dto.line2,
                        landmark = dto.landmark,
                        city = dto.city,
                        pinCode = dto.pinCode
                )

                fun fromDto(dto: UpdateAddressDto, address: TutorAddress): TutorAddress = address.copy(
                        line1 = dto.line1 ?: address.line1,
                        line2 = dto.line2 ?: address.line2,
                        landmark = dto.landmark ?: address.landmark,
                        city = dto.city ?: address.city,
                        pinCode = dto.pinCode ?: address.pinCode
                )
        }
}

@Entity
@Table(name = "tutor_qualification")
data class TutorQualification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "q_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "degree", nullable = false)
        var degree: String,

        @Column(name = "board", nullable = false)
        var board: String,

        @Column(name = "percentile", nullable = false)
        var percentile: Double,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor? = null

) {
        fun toDto(): QualificationDto? = QualificationDto(
                id = this.id ?: -1,
                degree = this.degree,
                board = this.board,
                percentile = this.percentile
        )

        companion object Util {
                fun fromDto(dto: CreateQualificationDto): TutorQualification = TutorQualification(
                        degree = dto.degree,
                        board = dto.board,
                        percentile = dto.percentile
                )
        }
}