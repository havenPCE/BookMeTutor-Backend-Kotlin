package com.pce.kotlin.bookmetutor.model.dto.tutor

import com.pce.kotlin.bookmetutor.model.dto.address.CreateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.qualification.CreateQualificationDto

class CreateTutorDto(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String?,
        val gender: String,
        val phone: String,
        val address: CreateAddressDto,
        val qualification: CreateQualificationDto
)