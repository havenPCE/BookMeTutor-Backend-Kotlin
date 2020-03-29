package com.pce.kotlin.bookmetutor.model.dao

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
        var id: Long?,

        @Column(name = "email", nullable = false, unique = true)
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
        var address: TutorAddress?,

        @OneToOne(mappedBy = "tutor", cascade = [CascadeType.ALL], orphanRemoval = true)
        var qualification: TutorQualification?,

        @OneToMany(mappedBy = "tutor", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        var bookings: Set<Booking> = emptySet()

)

@Entity
@Table(name = "tutor_address")
data class TutorAddress(
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

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor?
)

@Entity
@Table(name = "tutor_qualification")
data class TutorQualification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "q_id", unique = true, nullable = false)
        var id: Long?,

        @Column(name = "degree", nullable = false)
        var degree: String,

        @Column(name = "board", nullable = false)
        var board: String,

        @Column(name = "percentile", nullable = false)
        var percentile: Double,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "tutor_id")
        var tutor: Tutor?

)