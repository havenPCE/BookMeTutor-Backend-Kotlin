package com.pce.kotlin.bookmetutor.model.dao

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import kotlin.random.Random

data class TutorAddress(
        val id: Long = Random.nextLong(Long.MAX_VALUE),
        val line1: String,
        val line2: String? = null,
        val landmark: String? = null,
        val city: String,
        val pinCode: String
) {
    fun toDto() = AddressDto(
            id = this.id,
            line1 = this.line1,
            line2 = this.line2,
            landmark = this.landmark,
            city = this.city,
            pinCode = this.pinCode
    )

    companion object {
        fun fromDto(dto: CreateAddressDto) = TutorAddress(
                line1 = dto.line1,
                line2 = dto.line2,
                landmark = dto.landmark,
                city = dto.city,
                pinCode = dto.pinCode
        )

        fun fromDto(dto: UpdateAddressDto, default: BookingAddress) = TutorAddress(
                id = default.id,
                line1 = dto.line1 ?: default.line1,
                line2 = dto.line2 ?: default.line2,
                landmark = dto.landmark ?: default.landmark,
                city = dto.city ?: default.city,
                pinCode = dto.pinCode ?: default.pinCode
        )
    }
}
