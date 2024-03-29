package com.pce.kotlin.bookmetutor.model.dto.address

data class AddressDto(
    val id: Long,
    val line1: String,
    val line2: String? = null,
    val landmark: String? = null,
    val city: String,
    val pinCode: String
)