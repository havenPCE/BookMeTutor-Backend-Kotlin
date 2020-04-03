package com.pce.kotlin.bookmetutor.model.dao

import javax.persistence.*

@Entity
@Table(name = "admin")
data class Admin(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        var email: String,

        var password: String

)