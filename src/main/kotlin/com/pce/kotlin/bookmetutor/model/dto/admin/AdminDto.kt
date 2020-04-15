package com.pce.kotlin.bookmetutor.model.dto.admin

data class AdminDto(
        val id: Long? = null,
        val email: String,
        val verified: Boolean
)