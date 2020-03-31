package com.pce.kotlin.bookmetutor.service

import com.pce.kotlin.bookmetutor.model.dao.Tutor
import com.pce.kotlin.bookmetutor.model.dao.TutorAddress
import com.pce.kotlin.bookmetutor.model.dto.address.UpdateAddressDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.CreateTutorDto
import com.pce.kotlin.bookmetutor.model.dto.tutor.UpdateTutorDto

interface TutorService {
    fun retrieveAllTutors(): List<Tutor>?

    fun retrieveTutor(email: String): Tutor?

    fun createTutor(tutor: CreateTutorDto): Tutor

    fun updateTutor(email: String, update: UpdateTutorDto): Tutor?

    fun removeTutor(email: String): Boolean

    fun addTutorPhone(email: String, phone: String): Tutor?

    fun removeTutorPhone(email: String, phone: String): Tutor?

    fun updateTutorAddress(id: Long, update: UpdateAddressDto): TutorAddress?
}