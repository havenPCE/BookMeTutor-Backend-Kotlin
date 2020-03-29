package com.pce.kotlin.bookmetutor.model.dto.address

data class CreateAddressDto(
        val line1: String,
        val line2: String?,
        val landmark: String?,
        val city: String,
        val pinCode: String
)