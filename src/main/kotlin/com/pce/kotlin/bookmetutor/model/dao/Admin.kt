package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.admin.AdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.CreateAdminDto
import com.pce.kotlin.bookmetutor.model.dto.admin.UpdateAdminDto
import kotlin.random.Random

data class Admin(
    val id: Long = Random.nextLong(Long.MAX_VALUE),
    val email: String,
    val password: String,
    val verified: Boolean = true
) {
    fun toDto() = AdminDto(
        id = this.id,
        email = this.email,
        verified = this.verified
    )

    companion object {
        fun fromDto(dto: CreateAdminDto) = Admin(
            email = dto.email,
            password = dto.password
        )

        fun fromDto(dto: UpdateAdminDto, default: Admin) = Admin(
            id = default.id,
            email = default.email,
            password = dto.password ?: default.password,
            verified = dto.verified ?: default.verified
        )
    }
}