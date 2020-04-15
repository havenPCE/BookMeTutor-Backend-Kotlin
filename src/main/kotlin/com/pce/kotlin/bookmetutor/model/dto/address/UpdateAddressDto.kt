package com.pce.kotlin.bookmetutor.model.dto.address

data class UpdateAddressDto(
        val line1: String? = null,
        val line2: String? = null,
        val landmark: String? = null,
        val city: String? = null,
        val pinCode: String? = null
)