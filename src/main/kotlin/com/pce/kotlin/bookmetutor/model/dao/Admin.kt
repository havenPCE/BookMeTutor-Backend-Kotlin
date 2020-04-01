package com.pce.kotlin.bookmetutor.model.dao

import javax.persistence.*

@Entity
@Table(name = "admin")
data class Admin(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        val email: String,

        val password: String

)