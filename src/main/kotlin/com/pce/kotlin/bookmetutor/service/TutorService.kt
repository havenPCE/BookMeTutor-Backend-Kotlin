package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dto.address.AddressDto
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.TutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto

interface TutorService {
    fun retrieveAllTutors(): List<TutorDto>
    fun retrieveTutor(email: String): TutorDto?
    fun retrieveTutor(id: Long): TutorDto?
    fun createTutor(dto: CreateTutorDto): TutorDto?
    fun updateTutor(email: String, dto: UpdateTutorDto): TutorDto?
    fun removeTutor(email: String): Boolean
    fun addTutorPhone(email: String, phone: String): TutorDto?
    fun removeTutorPhone(email: String, phone: String): TutorDto?
    fun updateTutorAddress(id: Long, dto: UpdateAddressDto): AddressDto?
}