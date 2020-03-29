package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.util.Gender
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "student")
data class Student(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "student_id", unique = true, nullable = false)
        var id: Long?,

        @Column(name = "email", unique = true, nullable = false)
        var email: String,

        @Column(name = "password", nullable = false)
        var password: String,

        @Column(name = "first_name", nullable = false)
        var firstName: String,

        @Column(name = "last_name")
        var lastName: String?,

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
)

@Entity
@Table(name = "student_address")
data class StudentAddress(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "address_id", unique = true, nullable = false)
        var id: Long?,

        @Column(name = "line_1", nullable = false)
        var line1: String,

        @Column(name = "line_2")
        var line2: String?,

        @Column(name = "landmark")
        var landmark: String?,

        @Column(name = "city", nullable = false)
        var city: String,

        @Column(name = "pin_code", nullable = false, length = 6)
        var pinCode: String,

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "student_id")
        var student: Student?
)