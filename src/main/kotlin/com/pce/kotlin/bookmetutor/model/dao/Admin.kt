package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import kotlin.random.Random

data class Admin(
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val email: String,
        val password: String
) {
    companion object {
        fun fromDto(dto: CreateAdminDto) = Admin(
                email = dto.email,
                password = dto.password
        )
    }
}