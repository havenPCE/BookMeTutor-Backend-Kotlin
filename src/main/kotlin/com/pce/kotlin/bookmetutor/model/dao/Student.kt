package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.student.CreateStudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.StudentDto
import com.pce.kotlin.bookmetutor.model.dto.student.UpdateStudentDto
import com.pce.kotlin.bookmetutor.util.Gender
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "student_id", unique = true, nullable = false)
        var id: Long? = null,

        @Column(name = "email", unique = true, nullable = false)
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

        @Column(name = "registered")
        var registered: LocalDateTime,

        @ElementCollection
        var phones: Set<String> = emptySet(),

        @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
        var addresses: Set<StudentAddress> = emptySet(),

        @OneToMany(mappedBy = "student", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
        var bookings: Set<Booking> = emptySet()
) {
    fun toDto(): StudentDto? = StudentDto(
            id = this.id ?: -1,
            email = this.email,
            password = this.password,
            firstName = this.firstName,
            lastName = this.lastName,
            gender = this.gender.name,
            verified = this.verified,
            registered = this.registered,
            phones = this.phones.map { it },
            addresses = this.addresses.mapNotNull { it.toDto() },
            bookings = this.bookings.mapNotNull { it.toDto() }
    )

    companion object Util {
        fun fromDto(dto: CreateStudentDto): Student = Student(
                email = dto.email,
                password = dto.password,
                firstName = dto.firstName,
                lastName = dto.lastName,
                gender = Gender.valueOf(dto.gender),
                phones = setOf(dto.phone),
                registered = LocalDateTime.now()
        )

        fun fromDto(dto: UpdateStudentDto, student: Student): Student = student.copy(
                password = dto.password ?: student.password,
                firstName = dto.firstName ?: student.firstName,
                lastName = dto.lastName ?: student.lastName
        )
    }
}

@Entity
@Table(name = "student_address")
data class StudentAddress(
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

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "student_id")
        var student: Student? = null
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
        fun fromDto(dto: CreateAddressDto): StudentAddress = StudentAddress(
                line1 = dto.line1,
                line2 = dto.line2,
                landmark = dto.landmark,
                city = dto.city,
                pinCode = dto.pinCode
        )

        fun fromDto(dto: UpdateAddressDto, address: StudentAddress): StudentAddress = address.copy(
                line1 = dto.line1 ?: address.line1,
                line2 = dto.line2 ?: address.line2,
                landmark = dto.landmark ?: address.landmark,
                city = dto.city ?: address.city,
                pinCode = dto.pinCode ?: address.pinCode
        )
    }
}